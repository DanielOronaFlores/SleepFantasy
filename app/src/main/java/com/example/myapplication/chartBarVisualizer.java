package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import DataAccess.SleepDataAccess;
import Database.DatabaseConnection;
import Dates.DateManager;
import ChartsViews.BarChartPainter;

public class chartBarVisualizer extends AppCompatActivity {
    FrameLayout container;
    private DatabaseConnection connection;
    private SleepDataAccess sleepDataAccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_bar_visualizer);

        connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();
        sleepDataAccess = new SleepDataAccess(connection);

        Intent intent = getIntent();
        int data = intent.getIntExtra("data", -1);
        int filter = intent.getIntExtra("filter", -1);
        String date = intent.getStringExtra("date");

        container = findViewById(R.id.chartBarView);
        setLayoutWidth(filter);

        int[] dataToShow = selectDataToShow(data, date, filter);
        float[] values = setChartValues(dataToShow);
        int[] colors = {R.color.barCharColor1, R.color.barCharColor2};

        String strMaxCount = "0";
        Log.d("dataToShow", String.valueOf(dataToShow.length));
        if (dataToShow.length == 0) {
            Log.d("dataToShow", "No hay datos para mostrar");
            Toast.makeText(this, "No hay datos para mostrar", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Log.d("dataToShow", "Hay datos para mostrar");
            strMaxCount = String.valueOf(findMaxValue(dataToShow));
        }

        DateManager dateManager = new DateManager();

        TextView maxCount = findViewById(R.id.maxCount);
        TextView startDate = findViewById(R.id.startDate);
        TextView endDate = findViewById(R.id.endDate);
        maxCount.setText(strMaxCount);

        if (isWeek(filter)) {
            String startDateText = dateManager.getPastWeek(date);
            startDate.setText(dateManager.monthDayOnly(startDateText));

            endDate.setText(dateManager.monthDayOnly(date));
        } else {
            startDate.setText(dateManager.getPastMonth(date));
            endDate.setText(date);
        }

        BarChartPainter barChartView = new BarChartPainter(this, colors, values);
        container.addView(barChartView);
    }

    private void setLayoutWidth(int filter){
        ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
        if (filter == 1) layoutParams.width = 350; //Semana = 7 dias.
        else layoutParams.width = 1500; //Mes = 30 dias.
        container.setLayoutParams(layoutParams);
    }
    private int[] selectDataToShow(int data, String date, int filter) {
        boolean isWeek = isWeek(filter);

        switch (data){
            case 0:
                return sleepDataAccess.getEfficiency(date, isWeek);
            case 1:
                return sleepDataAccess.getAwakeningAmount(date, isWeek);
            case 2:
                return sleepDataAccess.getLoudSoundsAmount(date, isWeek);
            case 3:
                return sleepDataAccess.getSuddenMovementsAmount(date, isWeek);
            case 4:
                return sleepDataAccess.getPositionChangesAmount(date, isWeek);
            default:
                return new int[0];
        }
    }
    private float[] setChartValues(int[] data){
        float[] values = new float[data.length];
        for (int i = 0; i < data.length; i++) {
            values[i] = data[i];
        }
        return values;
    }
    private static int findMaxValue(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("El array es nulo o vacío");
        }
        int maxValue = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxValue = array[i];
            }
        }
        return maxValue;
    }
    private boolean isWeek(int filter) {
        return filter == 1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.closeDatabase();
    }
}