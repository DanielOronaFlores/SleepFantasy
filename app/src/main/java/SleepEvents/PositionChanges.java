package SleepEvents;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class PositionChanges {
    private final SensorManager accelerometerManager;
    private final Sensor accelerometerSensor;
    private final float[] accelerometerValues = new float[3];
    private final float[] accelerometerXThreshold = new float[2];
    private final float[] accelerometerYThreshold = new float[2];
    private int totalPositionChanges = 0;

    public PositionChanges(Context context) {
        accelerometerManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = accelerometerManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        changeAccelerometerThreshold();
    }

    public int getTotalPositionChanges() {
        return totalPositionChanges;
    }

    public void registerPositionChangesListener() {
        accelerometerManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        if (isPositionChangeDetected()) {
            changeAccelerometerThreshold();
            totalPositionChanges++;
            accelerometerManager.unregisterListener(accelerometerListener);
            System.out.println("Se ha detectado un cambio de posición.");
        }
    }

    private void changeAccelerometerThreshold() {
        accelerometerXThreshold[0] = accelerometerValues[0] - 5;
        accelerometerXThreshold[1] = accelerometerValues[0] + 5;

        accelerometerYThreshold[0] = accelerometerValues[1] - 5;
        accelerometerYThreshold[1] = accelerometerValues[1] + 5;
    }

    private boolean isPositionChangeDetected() {
        return isXThresholdExceeded() && isYThresholdExceeded();
    }
    private boolean isXThresholdExceeded() {
        return accelerometerValues[0] < accelerometerXThreshold[0] || accelerometerValues[0] > accelerometerXThreshold[1];
    }
    private boolean isYThresholdExceeded() {
        return accelerometerValues[1] < accelerometerYThreshold[0] || accelerometerValues[1] > accelerometerYThreshold[1];
    }

    private final SensorEventListener accelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            accelerometerValues[0] = event.values[0];
            accelerometerValues[1] = event.values[1];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}
