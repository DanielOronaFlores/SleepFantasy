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

import AppContext.MyApplication;
import Notifications.Notifications;

public class PostureSensor extends Service {
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer, sensorGyroscope, sensorHeartRate;
    private Handler handler;
    private Intent sleepTrackerServiceIntent;
    private boolean isHorizontal = false;
    private boolean isLowRotation = false;
    private boolean isHeartRateLow = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorHeartRate = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        if (sensorAccelerometer == null || sensorGyroscope == null || sensorHeartRate == null) stopSelf();

        handler = new Handler(Looper.getMainLooper());

        sleepTrackerServiceIntent = new Intent(MyApplication.getAppContext(), SleepTracker.class);
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("Posture Sensor Service started");
        Notification notification = Notifications.getNotification();
        startForeground(1, notification);

        runnable.run();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(runnable);
        sensorManager.unregisterListener(sensorListenerAccelerometer);
        sensorManager.unregisterListener(sensorListenerGyroscope);
        sensorManager.unregisterListener(sensorListenerHeartRate);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sensorManager.registerListener(sensorListenerAccelerometer, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(sensorListenerGyroscope, sensorGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(sensorListenerHeartRate, sensorHeartRate, SensorManager.SENSOR_DELAY_NORMAL);

            System.out.println("----------POSICION----------");
            System.out.println("isHorizontal: " + isHorizontal);
            System.out.println("isLowRotation: " + isLowRotation);
            System.out.println("isHeartRateLow: " + isHeartRateLow);

            if (isUserLyingDown()) {
                startService(sleepTrackerServiceIntent);
                stopSelf();
            }

            System.out.println("-----------------------------");
            long delay = 5000;
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
            isHeartRateLow = event.values[0] < 65;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private boolean isUserLyingDown() {
        return isHeartRateLow && isLowRotation;
    }
}