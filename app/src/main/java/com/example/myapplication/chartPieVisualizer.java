package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import DataAccess.SleepDataAccess;
import Database.DatabaseConnection;
import ChartsViews.PieChartView;

public class chartPieVisualizer extends AppCompatActivity {
    private DatabaseConnection connection;
    private SleepDataAccess sleepDataAccess;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_pie_visualizer);

        connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();
        sleepDataAccess = new SleepDataAccess(connection);

        FrameLayout container = findViewById(R.id.lineChartContainer);

        Intent intent = getIntent();
        String date = intent.getStringExtra("date");

        float deepSleep = getDeepSleepHours(date);
        float lightSleep = getLightSleepHours(date);
        float remSleep = getRemSleepHours(date);
        float vigil = getVigilHours(date);

        float[] values = {deepSleep, lightSleep, remSleep, vigil};
        int[] colors = {R.color.deepSleep, R.color.lightSleep, R.color.remSleep, R.color.vigil};

        PieChartView pieChartView = new PieChartView(this, colors, values);
        container.addView(pieChartView);

        TextView title = findViewById(R.id.chartTitle);
        title.setText(date);

        TextView vigilText = findViewById(R.id.vigilText);
        vigilText.setText(formatNumber(vigil) + "%");

        TextView lightText = findViewById(R.id.lightText);
        lightText.setText(formatNumber(lightSleep) + "%");

        TextView deepText = findViewById(R.id.deepText);
        deepText.setText(formatNumber(deepSleep) + "%");

        TextView remText = findViewById(R.id.remText);
        remText.setText(formatNumber(remSleep) + "%");
    }

    private float getDeepSleepHours(String date) {
        float deepSleep = sleepDataAccess.getDeepSleepTime(date);
        float totalSleep = sleepDataAccess.totalSleepTime(date);
        return (deepSleep / totalSleep) * 100;
    }
    private float getLightSleepHours(String date) {
        float lightSleep = sleepDataAccess.getlightSleepTime(date);
        float totalSleep = sleepDataAccess.totalSleepTime(date);
        return (lightSleep / totalSleep) * 100;
    }
    private float getRemSleepHours(String date) {
        float remSleep = sleepDataAccess.getRemSleepTime(date);
        float totalSleep = sleepDataAccess.totalSleepTime(date);
        return (remSleep / totalSleep) * 100;
    }
    private float getVigilHours(String date) {
        float vigil = sleepDataAccess.getVigilTime(date);
        float totalSleep = sleepDataAccess.totalSleepTime(date);
        return (vigil / totalSleep) * 100;
    }

    private String formatNumber(float number) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(number);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.closeDatabase();
    }
}