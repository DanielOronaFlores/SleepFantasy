package Services;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;

import AppContext.MyApplication;

public class PostureSensor extends Service {
    private SensorManager managerAccelerometer;
    private SensorManager managerGyroscope;
    private Sensor sensorAccelerometer;
    private Sensor sensorGyroscope;
    private Handler handler;
    private Intent sleepTrackerServiceIntent;
    private boolean isHorizontal = false;
    private boolean isLowRotation = false;
    private boolean isSleepTrackerRunning = false;

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
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            managerAccelerometer.registerListener(sensorListenerAccelerometer, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            managerGyroscope.registerListener(sensorListenerGyroscope, sensorGyroscope, SensorManager.SENSOR_DELAY_NORMAL);

            //TODO: Debemos poner una condicional usando el MRC para ejecutar el servicio de SleepTracker
            if (isUserLyingDown()) {
                if (!isSleepTrackerRunning) {
                    startService(sleepTrackerServiceIntent);
                    isSleepTrackerRunning = true;
                }
            } else {
                stopService(sleepTrackerServiceIntent);
                isSleepTrackerRunning = false;
            }
            //Log.d("PostureSensor", "isSleepTrackerRunning: " + isSleepTrackerRunning);

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

            if (xRotationRate != 0.0f || yRotationRate != 0.0f || zRotationRate != 0.0f) {
                double magnitude = Math.sqrt(xRotationRate * xRotationRate + yRotationRate * yRotationRate + zRotationRate * zRotationRate);
                double threshold = 0.5;
                isLowRotation = magnitude < threshold;
            }

        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private boolean isUserLyingDown() {
        return isHorizontal && isLowRotation;
    }
}
