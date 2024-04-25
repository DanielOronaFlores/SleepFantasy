package SleepEvents;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SuddenMovements {
    private final SensorManager sensorManager;
    private final Sensor gyroscopeSensor;
    boolean canDetectSuddenMovement = true;
    int totalSuddenMovements = 0;

    public SuddenMovements(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    public int getTotalSuddenMovements() {
        return totalSuddenMovements;
    }

    public void registerSuddenMovementsListener() {
        if (gyroscopeSensor != null) {
            sensorManager.registerListener(sensorListenerGyroscope, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public boolean isSuddenMovement(float xAxis, float yAxis, float zAxis) {
        if (xAxis != 0.0f || yAxis != 0.0f || zAxis != 0.0f) {
            double magnitude = Math.sqrt(xAxis * xAxis + yAxis * yAxis + zAxis * zAxis);
            double threshold = 5;
            return magnitude > threshold;
        }
        sensorManager.unregisterListener(sensorListenerGyroscope);
        return false;
    }

    private final SensorEventListener sensorListenerGyroscope = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float xRotationRate = Math.abs(event.values[0]);
            float yRotationRate = Math.abs(event.values[1]);
            float zRotationRate = Math.abs(event.values[2]);

            if (isSuddenMovement(xRotationRate, yRotationRate, zRotationRate) && canDetectSuddenMovement) {
                totalSuddenMovements++;
                canDetectSuddenMovement = false;
                System.out.println("Se ha detectado un movimiento brusco.");
            } else {
                canDetectSuddenMovement = true;
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}
