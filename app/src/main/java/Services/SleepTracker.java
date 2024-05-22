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
import java.util.Arrays;
import java.util.List;

import AudioFilter.AudioFilter;
import Database.DataAccess.PreferencesDataAccess;
import Database.DataUpdates.SleepDataUpdate;
import Database.DatabaseConnection;
import Calculators.AverageCalculator;
import Calculators.SleepCycle;
import Files.AudiosPaths;
import Files.StorageManager;
import GameManagers.Challenges.ChallengesUpdater;
import GameManagers.Monsters.MonsterConditions;
import Models.Sound;
import Notifications.Notifications;
import Recorders.PCMRecorder;
import Recorders.Preferences.RecordingPreferences;
import Recorders.Recorder;
import Serializers.Deserializer;
import SleepEvents.Awakenings;
import SleepEvents.PositionChanges;
import SleepEvents.SuddenMovements;
import SleepEvaluator.SleepEvaluator;

public class SleepTracker extends Service {
    private DatabaseConnection connection;
    private SleepDataUpdate sleepDataUpdate;
    private PreferencesDataAccess preferencesDataAccess;
    private SleepCycle sleepCycleCalculator;
    private AverageCalculator averageCalculator;
    private List<Double> bpmList;
    private List<Float> lightList;
    private SensorManager sensorManager;
    private Sensor heartRateSensor, lightSensor, accelerometerSensor;
    private PCMRecorder pcmRecorder;
    private Recorder recorder;
    private Handler handler;
    private PowerManager.WakeLock wakeLock;

    // Sleep tracking variables
    private double bpm;
    private int currentSleepPhase, timeAwake;
    private float minuteCounter;
    private boolean isSleeping, isEventRunning;
    int awakeningsBeforeThreshold;

    // Sleep events
    private Handler eventsHandler;
    private SuddenMovements suddenMovements;
    private PositionChanges positionChanges;
    private Awakenings awakenings;
    private int awakeningsAmount;

    // Sleep data variables
    private int vigilTime, lightSleepTime, deepSleepTime, remSleepTime;
    private float light;
    private int lastHourMovements;
    boolean isVertical;

    // Monsters variables
    private final boolean[] monsterConditions = new boolean[5];

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("WakelockTimeout")
    @Override
    public void onCreate() {
        super.onCreate();

        initializeComponents();
        acquireWakeLock();
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("Rastreo de sueno iniciado");
        startForegroundService();
        initializeVariables();

        if (preferencesDataAccess.getRecordAudios()) {
            if (StorageManager.isInsufficientStorage()) {
                Notifications.showLowStorageNotification();
            } else {
                startRecording();
            }
        }

        startTracking();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Deteniendo servicio");

        stopRecording();
        filterAudio();
        unregisterListeners();
        performDataOperations();

        releaseWakeLock();
        stopForeground(true);
    }

    private void initializeComponents() {

        connection = DatabaseConnection.getInstance(this);
        sleepDataUpdate = new SleepDataUpdate(connection);
        preferencesDataAccess = new PreferencesDataAccess(connection);
        sleepCycleCalculator = new SleepCycle();
        averageCalculator = new AverageCalculator();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (isMissingSensors()) {
            Notifications.showMissingSensorsNotification();
            stopSelf();
        }

        pcmRecorder = new PCMRecorder();
        recorder = new Recorder();

        handler = new Handler(Looper.getMainLooper());
        eventsHandler = new Handler(Looper.getMainLooper());

        suddenMovements = new SuddenMovements(this);
        positionChanges = new PositionChanges(this);
    }

    @SuppressLint("WakelockTimeout")
    private void acquireWakeLock() {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SleepTracker::lock");
        wakeLock.acquire();
    }

    @SuppressLint("ForegroundServiceType")
    private void startForegroundService() {
        Notification notification = createNotification();
        startForeground(1, notification);
    }

    private void initializeVariables() {
        isEventRunning = false;
        isSleeping = false;

        minuteCounter = 0.0f;
        currentSleepPhase = 0;

        awakeningsAmount = 0;
        awakeningsBeforeThreshold = 0;

        bpmList = new ArrayList<>();
        lightList = new ArrayList<>();
        awakenings = new Awakenings();

        Arrays.fill(monsterConditions, false);
    }

    private void startRecording() {
        pcmRecorder.startRecording();
        if (preferencesDataAccess.getSaveRecordings()) {
            recorder.startRecording();
        }
    }

    private void startTracking() {
        handler.post(runnable);
    }

    private void stopRecording() {
        if (pcmRecorder.isPlaying()) pcmRecorder.stopRecording();
        if (recorder.isRecording()) recorder.stopRecording();
    }

    private void filterAudio() {
        RecordingPreferences recordingPreferences = new RecordingPreferences();
        AudioFilter.startFilter(recordingPreferences.getPreferredSamplingRate());
    }

    private void unregisterListeners() {
        sensorManager.unregisterListener(heartRateListener);
        sensorManager.unregisterListener(lightListener);
        sensorManager.unregisterListener(accelerometerListener);
        handler.removeCallbacks(runnable);
        eventsHandler.removeCallbacks(events);
    }

    private void performDataOperations() {
        System.out.println("----------DATOS OBTENIDOS EN CICLO--------------");
        System.out.println("Tiempo de vigilia: " + vigilTime);
        System.out.println("Tiempo de sueño ligero: " + lightSleepTime);
        System.out.println("Tiempo de sueño profundo: " + deepSleepTime);
        System.out.println("Tiempo de sueño REM: " + remSleepTime);

        if (lightSleepTime > 0 && deepSleepTime > 0 && remSleepTime > 0) {
            List<Sound> soundsList = Deserializer.deserializeFromXML(AudiosPaths.getListSoundsPath());

            int suddenMovementsCount = suddenMovements.getTotalSuddenMovements();
            int positionChangesCount = positionChanges.getTotalPositionChanges();
            int loudSoundsAmount = soundsList.size();
            int lightMean = (int) averageCalculator.calculateMeanFloat(lightList);


            System.out.println("Cantidad de movimientos bruscos: " + suddenMovementsCount);
            System.out.println("Cantidad de cambios de posición: " + positionChangesCount);
            System.out.println("Cantidad de sonidos fuertes: " + loudSoundsAmount);
            System.out.println("Cantidad de despertares: " + awakeningsAmount);
            System.out.println("Luz obtenida: " + lightMean);

            sleepDataUpdate.insertData(vigilTime, // Insterta los datos en la base de datos
                    lightSleepTime,
                    deepSleepTime,
                    remSleepTime,
                    lightMean,
                    loudSoundsAmount,
                    suddenMovementsCount,
                    positionChangesCount,
                    awakeningsAmount);

            SleepEvaluator sleepEvaluator = new SleepEvaluator(); // Evalua la calidad del sueño
            sleepEvaluator.evaluate(vigilTime,
                    lightSleepTime,
                    deepSleepTime,
                    remSleepTime,
                    awakeningsAmount,
                    suddenMovementsCount,
                    positionChangesCount,
                    monsterConditions);

            ChallengesUpdater challengesUpdater = new ChallengesUpdater(connection);
            challengesUpdater.updateSleepingConditions(); // Actualiza las condiciones de sueño

            //MissionsUpdater.updateMission4(lightMean);

        } else {
            System.out.println("No se han registrado datos");
            Notifications.showMissingDataNotification();
        }
    }

    private void releaseWakeLock() {
        wakeLock.release();
    }

    private boolean isMissingSensors() {
        return heartRateSensor == null || lightSensor == null || accelerometerSensor == null;
    }

    private Notification createNotification() {
        NotificationChannel channel;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channel = new NotificationChannel("sleep_tracker_channel",
                    "Sleep Tracker Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "sleep_tracker_channel")
                .setContentTitle("Sleep Tracker Service")
                .setContentText("Running");

        return builder.build();
    }

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
            if (event.values[0] != 0f) {
                light = event.values[0];
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private final SensorEventListener accelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            float x = Math.abs(values[0]);

            isVertical = x > 9.7;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    Runnable runnable = new Runnable() {
        private final int DELAY_TIME = 5000; // Valor en milisegundos (real)
        private final float realMinutes = 0.08333f; // Segundos equivalentes a DELAY_TIME / 60
        private final float virtualMinutes = 10; // Minutos que queremos que pasen en la aplicación
        private final float multiplier = virtualMinutes / realMinutes;

        @Override
        public void run() {
            sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            System.out.println("Parado " + isVertical);
            if (!isSleeping && isVertical) {
                Intent intent = new Intent(SleepTracker.this, PostureSensor.class);
                startService(intent);
                stopSelf();
            }

            if (isSleeping && !isEventRunning) {
                events.run();
                isEventRunning = true;
                lastHourMovements = 0;
                System.out.println("Eventos: se han iniciado los eventos");
            }

            System.out.println("Eventos corriendo: " + isEventRunning);

            sensorManager.registerListener(heartRateListener, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
            if (bpm > 0.0) bpmList.add(bpm);

            double bpmMean = 0;
            if (minuteCounter % 5 == 0) { // 5 minutos
                bpmMean = averageCalculator.calculateMeanDouble(bpmList);
                System.out.println("Promedio de LPM: " + bpmMean);
                System.out.println("Fase de sueño actual: " + currentSleepPhase);

                if (currentSleepPhase == 0) { // Despierto
                    currentSleepPhase = sleepCycleCalculator.hasVigilEnded(bpmMean) ? 1 : 0;
                    vigilTime += (int) (realMinutes * multiplier);
                    System.out.println("Tiempo vigilia: " + vigilTime);
                } else if (currentSleepPhase == 1) { // Ligero
                    isSleeping = true;
                    currentSleepPhase = sleepCycleCalculator.hasLightEnded(bpmMean) ? 2 : 1;
                    lightSleepTime += (int) (realMinutes * multiplier);
                    System.out.println("Tiempo de sueño ligero: " + lightSleepTime);
                } else if (currentSleepPhase == 2) { // Profundo
                    currentSleepPhase = sleepCycleCalculator.hasDeepEnded(bpmMean) ? 3 : 2;
                    deepSleepTime += (int) (realMinutes * multiplier);
                    System.out.println("Tiempo de sueño profundo: " + deepSleepTime);
                } else if (currentSleepPhase == 3) { // REM
                    currentSleepPhase = sleepCycleCalculator.hasREMEnded(bpmMean) ? 1 : 3;
                    remSleepTime += (int) (realMinutes * multiplier);
                    System.out.println("Tiempo de sueño REM: " + remSleepTime);
                }

                if (isSleeping) {
                    if (awakenings.isAwakening(isVertical, currentSleepPhase)) {
                        awakeningsAmount++;
                        awakeningsBeforeThreshold++;
                        timeAwake += (int) (realMinutes * multiplier);
                        System.out.println("Se ha detectado un despertar");
                        System.out.println("Tiempo despierto: " + timeAwake);
                    } else {
                        awakeningsBeforeThreshold = 0;
                        timeAwake = 0;
                    }
                }
                bpmList.clear();
            }

            if (minuteCounter % 30 == 0) { // 30 = 30 minutos
                sensorManager.registerListener(lightListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
                lightList.add(light);
                System.out.println("Se ha registrado la luz: " + light);
            }

            // ----- MONSTRUOS -----
            if (minuteCounter % 60 == 0) { // 60 = 1 hora
                if (MonsterConditions.isAnxiety((int) bpmMean, positionChanges.getTotalPositionChanges(), suddenMovements.getTotalSuddenMovements())) {
                    monsterConditions[2] = true;
                    System.out.println("Monstruos: ha aparecido un monstruo por ansiedad");
                }
                if (MonsterConditions.isNightmare((int) bpmMean, suddenMovements.getTotalSuddenMovements())) {
                    monsterConditions[3] = true;
                    System.out.println("Monstruos: ha aparecido un monstruo por pesadilla");
                }
            }

            if (MonsterConditions.isSomnambulism(currentSleepPhase, isVertical)) {
                monsterConditions[4] = true;
                System.out.println("Monstruos: ha aparecido un monstruo por sonambulismo");
            }
            // ----- MONSTRUOS -----

            if (timeAwake > 15) {
                sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                System.out.println("Vertical: " + isVertical);

                if (isVertical) {
                    awakeningsAmount -= awakeningsBeforeThreshold;
                    stopSelf();
                }
            }

            System.out.println("Minutos: " + minuteCounter);
            System.out.println("----------------------------------------------------");
            minuteCounter += realMinutes * multiplier; // 0.5 minutos = 30 s
            handler.postDelayed(this, DELAY_TIME);
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
}