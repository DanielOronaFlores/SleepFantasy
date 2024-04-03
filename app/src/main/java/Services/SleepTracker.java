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

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

import AudioFilter.AudioFilter;
import Database.DataUpdates.SleepDataUpdate;
import Database.DatabaseConnection;
import Calculators.AverageCalculator;
import Calculators.SleepCycle;
import Files.AudiosPaths;
import GameManagers.Challenges.ChallengesUpdater;
import GameManagers.Missions.MissionsUpdater;
import Models.Sound;
import Recorders.PCMRecorder;
import Recorders.Recorder;
import Serializers.Deserializer;
import SleepEvents.Awakenings;
import SleepEvents.PositionChanges;
import SleepEvents.SuddenMovements;
import SleepEvaluator.SleepEvaluator;

public class SleepTracker extends Service {
    private DatabaseConnection connection;
    private SleepDataUpdate sleepDataUpdate;
    private SleepCycle sleepCycleCalculator;
    private AverageCalculator averageCalculator;
    private List<Double> bpmList;
    private List<Float> lightList;
    private SensorManager heartRateManager, lightManager;
    private PCMRecorder pcmRecorder;
    private Recorder recorder;
    private Sensor heartRateSensor, lightSensor;
    private Handler handler;
    private PowerManager.WakeLock wakeLock;

    // Variables de rastreo de sueño
    private double bpm;
    private int minuteCounter, currentSleepPhase, timeAwake;
    private boolean isSleeping, isEventRunning;

    // Eventos de sueño
    private Handler eventsHandler;
    private SuddenMovements suddenMovements;
    private PositionChanges positionChanges;
    private Awakenings awakenings;
    private int awakeningsAmount = 0;

    // Variables de datos de sueño
    private int vigilTime, lightSleepTime, deepSleepTime, remSleepTime;
    private float light;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("WakelockTimeout")
    @Override
    public void onCreate() {
        super.onCreate();

        connection = DatabaseConnection.getInstance(this);
        sleepDataUpdate = new SleepDataUpdate(connection);
        sleepCycleCalculator = new SleepCycle();
        averageCalculator = new AverageCalculator();

        heartRateManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        heartRateSensor = heartRateManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        lightManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = lightManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (isMissingSensors()) stopSelf();

        pcmRecorder = new PCMRecorder();
        recorder = new Recorder();

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
        Notification notification = createNotification();
        startForeground(1, notification);

        isEventRunning = false;
        isSleeping = false;

        minuteCounter = 0;
        currentSleepPhase = 0;

        bpmList = new ArrayList<>();
        lightList = new ArrayList<>();
        awakenings = new Awakenings();

        pcmRecorder.startRecording();
        recorder.startRecording();

        runnable.run();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        pcmRecorder.stopRecording();
        recorder.stopRecording();

        AudioFilter audioFilter = new AudioFilter();
        audioFilter.filterAudio();

        heartRateManager.unregisterListener(heartRateListener);
        lightManager.unregisterListener(heartRateListener);

        handler.removeCallbacks(runnable);
        eventsHandler.removeCallbacks(events);

        if (lightSleepTime != 0) {
            AudiosPaths audiosFiles = new AudiosPaths();
            Deserializer deserializer = new Deserializer();
            List<Sound> soundsList = deserializer.deserializeFromXML(audiosFiles.getXMLPath());

            int suddenMovements = this.suddenMovements.getTotalSuddenMovements();
            int positionChanges = this.positionChanges.getTotalPositionChanges();
            int loudSoundsAmount = soundsList.size();

            connection.openDatabase();

            sleepDataUpdate.insertData(vigilTime,
                    lightSleepTime,
                    deepSleepTime,
                    remSleepTime,
                    averageCalculator.calculateMeanFloat(lightList),
                    loudSoundsAmount,
                    suddenMovements,
                    positionChanges,
                    awakeningsAmount);

            SleepEvaluator sleepEvaluator = new SleepEvaluator();
            sleepEvaluator.evaluate(vigilTime,
                    lightSleepTime,
                    deepSleepTime,
                    remSleepTime,
                    awakeningsAmount,
                    suddenMovements,
                    positionChanges);

            ChallengesUpdater challengesUpdater = new ChallengesUpdater(connection);
            challengesUpdater.updateSleepingConditions();

            MissionsUpdater missionsUpdater = new MissionsUpdater();
            missionsUpdater.updateMission4(light);

            connection.closeDatabase();
        }

        stopForeground(true);
        wakeLock.release();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isSleeping && !isEventRunning) {
                events.run();
                isEventRunning = true;
            }

            heartRateManager.registerListener(heartRateListener, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
            if (bpm > 0.0) bpmList.add(bpm);

            // Registar valores MRC
            if (bpmList.size() == 10) { // 10 = 5 minutos
                minuteCounter += 5;
                double bpmMean = averageCalculator.calculateMeanDouble(bpmList);

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

                if (isSleeping) {
                    if (awakenings.isAwakening(bpmMean, currentSleepPhase)) {
                        awakeningsAmount++;
                        timeAwake += 5;
                    } else {
                        timeAwake = 0;
                    }
                }
                bpmList.clear();
            }

            // Registrar valores de luz
            if (minuteCounter == 30) { // 30 = 30 minutos
                lightManager.registerListener(lightListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
                if (light != 0f) lightList.add(light);
                lightManager.unregisterListener(heartRateListener);
                minuteCounter = 0;
            }

            int delay = 30000; // 30000 ms = 30 s
            if (timeAwake >= 20) { // 20 minutos
                timeAwake -= 20;
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

    private boolean isMissingSensors() {
        return heartRateSensor == null || lightSensor == null;
    }
}