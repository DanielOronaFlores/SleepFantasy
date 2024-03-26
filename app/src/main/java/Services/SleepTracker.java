package Services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Database.DataUpdates.SleepDataUpdate;
import Database.DatabaseConnection;
import Dates.HoursManager;
import Serializers.Serializer;
import Calculators.HRVCalculator;
import Calculators.AverageCalculator;
import Calculators.SleepCycle;
import SleepEvents.Awakenings;
import SleepEvents.PositionChanges;
import SleepEvents.SuddenMovements;

public class SleepTracker extends Service {
    private final DatabaseConnection connection = DatabaseConnection.getInstance(this);
    private final SleepDataUpdate sleepDataUpdate = new SleepDataUpdate(connection);
    private final SleepCycle sleepCycleCalculator = new SleepCycle();
    private final HRVCalculator sdnnCalculator = new HRVCalculator();
    private final AverageCalculator averageCalculator = new AverageCalculator();
    private final HoursManager hoursManager = new HoursManager();
    private final List<Double> bpmList = new ArrayList<>(), rrIntervals = new ArrayList<>();
    private final List<Float> lightList = new ArrayList<>();
    private SensorManager heartRateManager, lightManager;
    private Sensor heartRateSensor, lightSensor;
    private Handler handler;
    private PowerManager.WakeLock wakeLock;

    // Variables de rastreo de sueño
    private double bpm, rrInterval;
    private int minuteCounter, currentSleepPhase;
    private boolean isSleeping = false;
    private boolean isEventRunning = false;

    // Eventos de sueño
    private Handler eventsHandler;
    private SuddenMovements suddenMovements;
    private PositionChanges positionChanges;
    private Awakenings awakenings = new Awakenings();
    private int awakeningsAmount = 0;

    // Variables de datos de sueño
    private int vigilTime, lightSleepTime, deepSleepTime, remSleepTime;
    private float light;


    // Para pruebas
    private final List<Models.SleepCycle> sleepCycleTestList = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("WakelockTimeout")
    @Override
    public void onCreate() {
        super.onCreate();

        heartRateManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        heartRateSensor = heartRateManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        lightManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = lightManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (heartRateSensor == null) stopSelf();;

        handler = new Handler(Looper.getMainLooper());
        eventsHandler = new Handler(Looper.getMainLooper());

        suddenMovements = new SuddenMovements(this);
        positionChanges = new PositionChanges(this);

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SleepTracker::lock");
        wakeLock.acquire();
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SleepTracker", "Iniciando servicio de rastreo de sueño");

        Notification notification = createNotification();
        startForeground(1, notification);

        minuteCounter = 0;
        currentSleepPhase = 0;

        runnable.run();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("SleepTracker", "Deteniendo servicio de rastreo de sueño");

        Serializer serializer = new Serializer();
        serializer.serializeSleepCycleToXML(sleepCycleTestList);

        heartRateManager.unregisterListener(heartRateListener);
        lightManager.unregisterListener(heartRateListener);

        handler.removeCallbacks(runnable);
        eventsHandler.removeCallbacks(events);

        int suddenMovements = this.suddenMovements.getTotalSuddenMovements();
        int positionChanges = this.positionChanges.getTotalPositionChanges();

        connection.openDatabase();
        sleepDataUpdate.insertData(vigilTime,
                lightSleepTime,
                deepSleepTime,
                remSleepTime,
                averageCalculator.calculateMeanFloat(lightList),
                suddenMovements,
                positionChanges);

        stopForeground(true);
        wakeLock.release();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isSleeping && !isEventRunning) {
                events.run();
                isEventRunning = true;
            }

            heartRateManager.registerListener(heartRateListener, heartRateSensor, SensorManager.SENSOR_DELAY_FASTEST);
            if (bpm > 0.0) bpmList.add(bpm);
            rrIntervals.add(rrInterval);

            // Registar valores SDNN y MRC
            if (bpmList.size() == 10) { // 10 = 5 minutos
                minuteCounter += 5;
                double bpmMean = averageCalculator.calculateMeanDouble(bpmList);

                double rrMean = averageCalculator.calculateMeanDouble(rrIntervals);
                double rrSummation = sdnnCalculator.calculateRRSummation(rrIntervals, rrMean);
                double sdnn = sdnnCalculator.calculateSDNN(rrIntervals, rrSummation);
                double rmssd = sdnnCalculator.calculateRMSSD(rrIntervals);
                double hrv = sdnnCalculator.calculateHRV(sdnn, rmssd);

                if (currentSleepPhase == 0) { // Despierto
                    currentSleepPhase = sleepCycleCalculator.hasAwakeningEnded(bpmMean) ? 1 : 0;
                    vigilTime += 5;
                }
                if (currentSleepPhase == 1) { // Ligero
                    isSleeping = true;
                    currentSleepPhase = sleepCycleCalculator.hasLightEnded(bpmMean) ? 2 : 1;
                    lightSleepTime += 5;
                }
                if (currentSleepPhase == 2) { // Profundo
                    currentSleepPhase = sleepCycleCalculator.hasDeepEnded(bpmMean) ? 3 : 2;
                    deepSleepTime += 5;
                }
                if (currentSleepPhase == 3) { // REM
                    currentSleepPhase = sleepCycleCalculator.hasREMEnded(bpmMean) ? 1 : 3;
                    remSleepTime += 5;
                }


                // Almacenar valores de prueba
                // ------------
                String phase = "";
                switch (currentSleepPhase) {
                    case 0:
                        phase = "Despierto";
                        break;
                    case 1:
                        phase = "Ligero";
                        break;
                    case 2:
                        phase = "Profundo";
                        break;
                    case 3:
                        phase = "REM";
                        break;
                }

                sleepCycleTestList.add(new Models.SleepCycle(
                        hoursManager.getCurrentHour(),
                        phase,
                        bpmMean,
                        sdnn,
                        hrv));
                //----------------

                if (awakenings.isAwakening(bpmMean, currentSleepPhase)) awakeningsAmount++;

                rrIntervals.clear();
                bpmList.clear();
            }

            // Registrar valores de luz
            if (minuteCounter == 30) { // 30 = 30 minutos
                if (light != 0f) lightList.add(light);
                lightManager.registerListener(lightListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
                minuteCounter = 0;
            }

            int delay = 30000; // 30000 ms = 30 s
            if (isStopTime()) {
                stopSelf();
            } else {
                handler.postDelayed(this, delay);
            }
        }
    };

    Runnable events = new Runnable() {
        @Override
        public void run() {
            suddenMovements.registerSuddenMovementsListener();
            positionChanges.registerPositionChangesListener();

            eventsHandler.postDelayed(this, 1000); // 1000 ms = 1 s
        }
    };

    private final SensorEventListener heartRateListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            bpm = event.values[0];
            rrInterval = 60000 / bpm;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private final SensorEventListener lightListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            light = event.values[0];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private Notification createNotification() {
        NotificationChannel channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("sleep_tracker_channel", "Sleep Tracker Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "sleep_tracker_channel")
                .setContentTitle("Sleep Tracker Service")
                .setContentText("Running");

        return builder.build();
    }


    //Placeholder for the actual stop time
    private boolean isStopTime() {
        int stopHour = 17;
        int stopMinute = 27;

        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(Calendar.MINUTE);

        return currentHour == stopHour && currentMinute >= stopMinute;
    }
}