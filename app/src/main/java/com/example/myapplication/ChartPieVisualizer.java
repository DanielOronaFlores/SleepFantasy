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
import ChartPainter.PieChartPainter;

public class ChartPieVisualizer extends AppCompatActivity {
    private final DatabaseConnection connection = DatabaseConnection.getInstance(this);;
    private final SleepDataAccess sleepDataAccess = new SleepDataAccess(connection);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_pie_visualizer);

        connection.openDatabase();
        FrameLayout container = findViewById(R.id.lineChartContainer);

        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        Log.d("ChatPieVisualizer", "Date: " + date);

        float totalSleepTime = sleepDataAccess.totalSleepTime(date);
        float deepSleepPercentage = convertDataToPercentage(sleepDataAccess.getDeepSleepTime(date), totalSleepTime);
        float lightSleepPercentage = convertDataToPercentage(sleepDataAccess.getLightSleepTime(date), totalSleepTime);
        float remSleepPercentage = convertDataToPercentage(sleepDataAccess.getRemSleepTime(date), totalSleepTime);
        float vigilPercentage = convertDataToPercentage(sleepDataAccess.getVigilTime(date), totalSleepTime);

        Log.d("ChatPieVisualizer", "Deep: " + deepSleepPercentage);
        Log.d("ChatPieVisualizer", "Light: " + lightSleepPercentage);
        Log.d("ChatPieVisualizer", "REM: " + remSleepPercentage);
        Log.d("ChatPieVisualizer", "Vigil: " + vigilPercentage);


        float[] values = {deepSleepPercentage, lightSleepPercentage, remSleepPercentage, vigilPercentage};
        int[] colors = {R.color.deepSleep, R.color.lightSleep, R.color.remSleep, R.color.vigil};

        PieChartPainter pieChartView = new PieChartPainter(this, colors, values);
        container.addView(pieChartView);

        TextView title = findViewById(R.id.chartTitle);
        title.setText(date);

        TextView vigilText = findViewById(R.id.vigilText);
        vigilText.setText(percentageFormat(vigilPercentage) + "%");

        TextView lightText = findViewById(R.id.lightText);
        lightText.setText(percentageFormat(lightSleepPercentage) + "%");

        TextView deepText = findViewById(R.id.deepText);
        deepText.setText(percentageFormat(deepSleepPercentage) + "%");

        TextView remText = findViewById(R.id.remText);
        remText.setText(percentageFormat(remSleepPercentage) + "%");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.closeDatabase();
    }

    private float convertDataToPercentage(float data, float total) {
        if (data == -1) {
            Toast.makeText(this, "Sin datos para esta fecha", Toast.LENGTH_SHORT).show();
            finish();
        }
        return (data / total) * 100;
    }

    private String percentageFormat(float number) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(number);
    }
}