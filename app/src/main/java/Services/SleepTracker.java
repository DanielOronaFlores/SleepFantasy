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
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Dates.HoursManager;
import Models.SleepCycle;
import Serializers.Serializer;
import SleepTracker.SDNNCalculator;
import Utils.AverageCalculator;
import SleepTracker.SleepCycleCalculator;

public class SleepTracker extends Service {
    private final SDNNCalculator sdnnCalculator = new SDNNCalculator();
    private final AverageCalculator averageCalculator = new AverageCalculator();
    private final SleepCycleCalculator sleepCycleCalculator = new SleepCycleCalculator();
    private final HoursManager hoursManager = new HoursManager();
    private final List<Double> bpmList = new ArrayList<>();
    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private Handler handler;
    private double bpm;

    // Para pruebas
    private final List<SleepCycle> sleepCycleTestList = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        if (heartRateSensor == null) stopSelf();

        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SleepTracker", "Iniciando servicio de rastreo de sueño");
        runnable.run();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("SleepTracker", "Deteniendo servicio de rastreo de sueño");

        sensorManager.unregisterListener(sensorListener);
        handler.removeCallbacks(runnable);

        Serializer serializer = new Serializer();
        serializer.test(sleepCycleTestList);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sensorManager.registerListener(sensorListener, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d("SleepTracker", "BPM: " + bpm);

            if (bpm > 0.0) bpmList.add(bpm); // Eliminar valores no registrados
            Log.d("SleepTracker", "bpmList: " + bpmList.size());

            if (bpmList.size() == 6) { // 1 minuto
                double bpmMean = averageCalculator.calculateMean(bpmList);

                sleepCycleTestList.add(new SleepCycle(hoursManager.getCurrentHour(),
                        sleepCycleCalculator.getSleepCycle(bpmMean),
                        bpmMean,
                        0));

                bpmList.clear();
            }

            // 10 segundos
            int delay = 10000;
            if (isStopTime()) stopSelf();
            else handler.postDelayed(this, delay);
        }
    };


    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            bpm = event.values[0];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };


    //Placeholder for the actual stop time
    private boolean isStopTime() {
        int stopHour = 14;
        int stopMinute = 35;

        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(Calendar.MINUTE);

        return currentHour == stopHour && currentMinute > stopMinute;
    }
}