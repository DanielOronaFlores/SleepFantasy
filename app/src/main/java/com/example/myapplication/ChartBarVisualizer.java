package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import Database.DataAccess.SleepDataAccess;
import Database.DatabaseConnection;
import Dates.DateManager;
import Painters.BarChartPainter;
import Styles.Themes;

public class ChartBarVisualizer extends AppCompatActivity {
    private DatabaseConnection connection;
    private SleepDataAccess sleepDataAccess;
    private TextView tvMaxCount, tvStartDate, tvEndDate;
    private FrameLayout containerGraph;
    private BarChartPainter barChartView;
    private String date;
    private int daysFilter;
    private int[] dataToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_bar_visualizer);

        connection = DatabaseConnection.getInstance(this);
        sleepDataAccess = new SleepDataAccess(connection);

        containerGraph = findViewById(R.id.chartBarView);
        tvMaxCount = findViewById(R.id.maxCount);
        tvStartDate = findViewById(R.id.startDate);
        tvEndDate = findViewById(R.id.endDate);

        Intent chartInformation = getIntent();
        int valueToShow = chartInformation.getIntExtra("data", -1);
        daysFilter = chartInformation.getIntExtra("filter", -1);
        date = chartInformation.getStringExtra("date");

        dataToShow = selectDataToShow(valueToShow, date, daysFilter);
        if (dataToShow.length == 0) {
            Toast.makeText(this, "No hay datos para mostrar", Toast.LENGTH_SHORT).show();
            finish();
        }

        float[] values = setChartValues(dataToShow);
        int[] colors = {R.color.barCharColor1, R.color.barCharColor2};

        barChartView = new BarChartPainter(this, colors, values);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String strMaxCount = String.valueOf(findMaxValue(dataToShow));
        tvMaxCount.setText(strMaxCount);

        if (daysFilter == 1) { // Es una semana (por el spinner de la actividad anterior
            String startDateText = DateManager.getDateSinceDate(7);

            tvStartDate.setText(DateManager.convertToFormat(startDateText, "MM-dd"));
            tvEndDate.setText(DateManager.convertToFormat(date, "MM-dd"));
        } else {
            tvStartDate.setText(DateManager.getDateSinceDate(30));
            tvEndDate.setText(date);
        }

        setLayoutWidth(dataToShow.length);
        containerGraph.addView(barChartView);

        setTheme();
    }

    private void setLayoutWidth(int length) {
        int width = 0;
        for (int i = 0; i < length; i++) {
            width += 50;
        }

        ViewGroup.LayoutParams layoutParams = containerGraph.getLayoutParams();
        layoutParams.width = width;
        containerGraph.setLayoutParams(layoutParams);
    }

    private int[] selectDataToShow(int valueToShow, String date, int filter) {
        boolean isWeek = (filter == 1);

        switch (valueToShow) {
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

    private float[] setChartValues(int[] data) {
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

    private void setTheme() {
        View view = findViewById(R.id.chartBarView);
        Themes.setBackgroundColor(this, view);
    }
}