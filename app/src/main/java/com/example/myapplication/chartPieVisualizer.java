package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import DataAccess.SleepDataAccess;
import Database.DatabaseConnection;
import ChartsViews.PieChartPainter;

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

        Log.d("deepSleep", String.valueOf(deepSleep));
        Log.d("lightSleep", String.valueOf(lightSleep));
        Log.d("remSleep", String.valueOf(remSleep));
        Log.d("vigil", String.valueOf(vigil));

        if (deepSleep == -1 || lightSleep == -1 || remSleep == -1 || vigil == -1) {
            Toast.makeText(this, "Sin datos para esta fecha", Toast.LENGTH_SHORT).show();
            finish();
        }

        float[] values = {deepSleep, lightSleep, remSleep, vigil};
        int[] colors = {R.color.deepSleep, R.color.lightSleep, R.color.remSleep, R.color.vigil};

        PieChartPainter pieChartView = new PieChartPainter(this, colors, values);
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
        if (deepSleep == -1) return -1;
        return (deepSleep / totalSleep) * 100;
    }
    private float getLightSleepHours(String date) {
        float lightSleep = sleepDataAccess.getLightSleepTime(date);
        float totalSleep = sleepDataAccess.totalSleepTime(date);
        if (lightSleep == -1) return -1;
        return (lightSleep / totalSleep) * 100;
    }
    private float getRemSleepHours(String date) {
        float remSleep = sleepDataAccess.getRemSleepTime(date);
        float totalSleep = sleepDataAccess.totalSleepTime(date);
        if (remSleep == -1) return -1;
        return (remSleep / totalSleep) * 100;
    }
    private float getVigilHours(String date) {
        float vigil = sleepDataAccess.getVigilTime(date);
        float totalSleep = sleepDataAccess.totalSleepTime(date);
        if (vigil == -1) return -1;
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