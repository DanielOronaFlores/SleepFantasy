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

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import AppContext.MyApplication;

public class PostureSensor extends Service {
    private SensorManager managerAccelerometer, managerGyroscope;
    private Sensor sensorAccelerometer, sensorGyroscope;
    private Handler handler;
    private Intent sleepTrackerServiceIntent;
    private boolean isHorizontal = false;
    private boolean isLowRotation = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        managerAccelerometer = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAccelerometer = managerAccelerometer.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        managerGyroscope = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorGyroscope = managerGyroscope.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        if (sensorAccelerometer == null || sensorGyroscope == null) stopSelf();

        handler = new Handler(Looper.getMainLooper());

        sleepTrackerServiceIntent = new Intent(MyApplication.getAppContext(), SleepTracker.class);
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("Posture Sensor Service started");
        Notification notification = createNotification();
        startForeground(1, notification);

        runnable.run();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(runnable);
        managerAccelerometer.unregisterListener(sensorListenerAccelerometer);
        managerGyroscope.unregisterListener(sensorListenerGyroscope);
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

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            managerAccelerometer.registerListener(sensorListenerAccelerometer, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            managerGyroscope.registerListener(sensorListenerGyroscope, sensorGyroscope, SensorManager.SENSOR_DELAY_NORMAL);

            System.out.println("isHorizontal: " + isHorizontal);
            System.out.println("isLowRotation: " + isLowRotation);

            if (isUserLyingDown()) {
                startService(sleepTrackerServiceIntent);
                stopSelf();
            }

            // 1 segundos
            long delay = 1000;
            handler.postDelayed(this, delay);
        }
    };

    private final SensorEventListener sensorListenerAccelerometer = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float xAxis = event.values[0];
            if (xAxis != 0.0f) {
                float threshold = 5.0f;
                isHorizontal = Math.abs(xAxis) < threshold;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private final SensorEventListener sensorListenerGyroscope = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float xRotationRate = event.values[0];
            float yRotationRate = event.values[1];
            float zRotationRate = event.values[2];

            double magnitude = Math.sqrt(xRotationRate * xRotationRate + yRotationRate * yRotationRate + zRotationRate * zRotationRate);
            double threshold = 0.5;
            isLowRotation = magnitude < threshold;

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private boolean isUserLyingDown() {
        return isHorizontal && isLowRotation;
    }
}
