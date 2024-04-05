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
import Database.DataAccess.PreferencesDataAccess;
import Database.DataUpdates.SleepDataUpdate;
import Database.DatabaseConnection;
import Calculators.AverageCalculator;
import Calculators.SleepCycle;
import Files.AudiosPaths;
import Files.StorageManager;
import GameManagers.Challenges.ChallengesUpdater;
import GameManagers.Missions.MissionsUpdater;
import Models.Sound;
import Notifications.Notifications;
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
    private PreferencesDataAccess preferencesDataAccess;
    private SleepCycle sleepCycleCalculator;
    private AverageCalculator averageCalculator;
    private List<Double> bpmList;
    private List<Float> lightList;
    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private Sensor lightSensor;
    private Notifications notifications;
    private PCMRecorder pcmRecorder;
    private Recorder recorder;
    private Handler handler;
    private PowerManager.WakeLock wakeLock;

    // Sleep tracking variables
    private double bpm;
    private int minuteCounter, currentSleepPhase, timeAwake;
    private boolean isSleeping, isEventRunning;

    // Sleep events
    private Handler eventsHandler;
    private SuddenMovements suddenMovements;
    private PositionChanges positionChanges;
    private Awakenings awakenings;
    private int awakeningsAmount = 0;

    // Sleep data variables
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

        // Initialize components
        initializeComponents();

        // Acquire wake lock
        acquireWakeLock();
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Start foreground service
        startForegroundService();

        // Initialize variables
        initializeVariables();

        // Start recording
        if (preferencesDataAccess.getRecordSnorings()) {
            if (StorageManager.isInsufficientStorage()) {
                notifications.showLowStorageNotification();
            } else {
                System.out.println("record = " + preferencesDataAccess.getRecordSnorings());
                startRecording();
            }
        }

        // Start tracking
        startTracking();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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

        if (isMissingSensors()) {
            stopSelf();
        }

        notifications = new Notifications();
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

        minuteCounter = 0;
        currentSleepPhase = 0;

        bpmList = new ArrayList<>();
        lightList = new ArrayList<>();
        awakenings = new Awakenings();
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
        AudioFilter audioFilter = new AudioFilter();
        audioFilter.filterAudio();
    }

    private void unregisterListeners() {
        sensorManager.unregisterListener(heartRateListener);
        sensorManager.unregisterListener(lightListener);
        handler.removeCallbacks(runnable);
        eventsHandler.removeCallbacks(events);
    }

    private void performDataOperations() {
        if (lightSleepTime != 0) {
            AudiosPaths audiosFiles = new AudiosPaths();
            Deserializer deserializer = new Deserializer();
            List<Sound> soundsList = deserializer.deserializeFromXML(audiosFiles.getListSoundsPath());

            int suddenMovementsCount = suddenMovements.getTotalSuddenMovements();
            int positionChangesCount = positionChanges.getTotalPositionChanges();
            int loudSoundsAmount = soundsList.size();

            connection.openDatabase();

            sleepDataUpdate.insertData(vigilTime,
                    lightSleepTime,
                    deepSleepTime,
                    remSleepTime,
                    averageCalculator.calculateMeanFloat(lightList),
                    loudSoundsAmount,
                    suddenMovementsCount,
                    positionChangesCount,
                    awakeningsAmount);

            SleepEvaluator sleepEvaluator = new SleepEvaluator();
            sleepEvaluator.evaluate(vigilTime,
                    lightSleepTime,
                    deepSleepTime,
                    remSleepTime,
                    awakeningsAmount,
                    suddenMovementsCount,
                    positionChangesCount);

            ChallengesUpdater challengesUpdater = new ChallengesUpdater(connection);
            challengesUpdater.updateSleepingConditions();

            MissionsUpdater missionsUpdater = new MissionsUpdater();
            missionsUpdater.updateMission4(light);

            connection.closeDatabase();
        }
    }

    private void releaseWakeLock() {
        wakeLock.release();
    }

    private boolean isMissingSensors() {
        return heartRateSensor == null || lightSensor == null;
    }

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

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isSleeping && !isEventRunning) {
                events.run();
                isEventRunning = true;
            }
            System.out.println("Minute counter: " + minuteCounter);

            sensorManager.registerListener(heartRateListener, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
            if (bpm > 0.0) bpmList.add(bpm);

            double bpmMean;
            if (bpmList.size() == 10) { // 10 = 5 minutos
                minuteCounter += 5;
                bpmMean = averageCalculator.calculateMeanDouble(bpmList);

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

            if (minuteCounter % 30 == 0) { // 30 = 30 minutos
                sensorManager.registerListener(lightListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
                if (light != 0f) lightList.add(light);
                sensorManager.unregisterListener(lightListener);
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
}