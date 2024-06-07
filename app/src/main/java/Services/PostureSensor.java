package Services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;

import Notifications.Notifications;

public class PostureSensor extends Service {
    private SensorManager sensorManager;
    private Sensor sensorGyroscope, sensorHeartRate, sensorAccelerometer;
    private Handler handler;
    private Intent sleepTrackerServiceIntent;
    private boolean isLowRotation;
    private boolean isHeartRateLow;
    private boolean isLyingDown;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isLowRotation = false;
        isHeartRateLow = false;
        isLyingDown = false;

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorHeartRate = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (sensorGyroscope == null || sensorHeartRate == null || sensorAccelerometer == null) stopSelf();
        handler = new Handler(Looper.getMainLooper());

        sleepTrackerServiceIntent = new Intent(PostureSensor.this, SleepTracker.class);
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = Notifications.getNotification();
        startForeground(1, notification);

        runnable.run();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(runnable);
        sensorManager.unregisterListener(sensorListenerGyroscope);
        sensorManager.unregisterListener(sensorListenerHeartRate);
        sensorManager.unregisterListener(sensorListenerAcceleremoeter);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sensorManager.registerListener(sensorListenerGyroscope, sensorGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(sensorListenerHeartRate, sensorHeartRate, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(sensorListenerAcceleremoeter, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

            if (isUserLyingDown()) {
                startService(sleepTrackerServiceIntent);
                stopSelf();
            }

            long delay = 3000;
            handler.postDelayed(this, delay);
        }
    };
    private final SensorEventListener sensorListenerGyroscope = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float xRotationRate = event.values[0];
            float yRotationRate = event.values[1];
            float zRotationRate = event.values[2];

            //Velocidad angular
            double magnitude = Math.sqrt(xRotationRate * xRotationRate + yRotationRate * yRotationRate + zRotationRate * zRotationRate);
            double threshold = sensorGyroscope.getMaximumRange() * 0.1;
            isLowRotation = magnitude < threshold;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private final SensorEventListener sensorListenerHeartRate = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            isHeartRateLow = event.values[0] < 65 && event.values[0] != 0.0f;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private final SensorEventListener sensorListenerAcceleremoeter = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            float x = Math.abs(values[0]);

            isLyingDown = x < 3.0f;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private boolean isUserLyingDown() {
        return isLowRotation && isHeartRateLow && isLyingDown;
    }
}