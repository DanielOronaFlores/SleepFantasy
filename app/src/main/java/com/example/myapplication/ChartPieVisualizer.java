package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import Database.DataAccess.SleepDataAccess;
import Database.DatabaseConnection;
import GameManagers.Challenges.ChallengesUpdater;
import Painters.PieChartPainter;
import Styles.Themes;

public class ChartPieVisualizer extends AppCompatActivity {
    private DatabaseConnection connection;
    private SleepDataAccess sleepDataAccess;
    private FrameLayout container;
    private TextView title, vigilText, lightText, deepText, remText;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_pie_visualizer);

        connection = DatabaseConnection.getInstance(this);
        sleepDataAccess = new SleepDataAccess(connection);

        container = findViewById(R.id.lineChartContainer);
        title = findViewById(R.id.chartTitle);
        vigilText = findViewById(R.id.vigilText);
        lightText = findViewById(R.id.lightText);
        deepText = findViewById(R.id.deepText);
        remText = findViewById(R.id.remText);

        Intent intent = getIntent();
        date = intent.getStringExtra("date");

    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        connection.openDatabase();

        float totalSleepTime = sleepDataAccess.totalSleepTime(date);
        float deepSleepPercentage = convertDataToPercentage(sleepDataAccess.getDeepSleepTime(date), totalSleepTime);
        float lightSleepPercentage = convertDataToPercentage(sleepDataAccess.getLightSleepTime(date), totalSleepTime);
        float remSleepPercentage = convertDataToPercentage(sleepDataAccess.getRemSleepTime(date), totalSleepTime);
        float vigilPercentage = convertDataToPercentage(sleepDataAccess.getVigilTime(date), totalSleepTime);

        float[] values = {deepSleepPercentage, lightSleepPercentage, remSleepPercentage, vigilPercentage};
        int[] colors = {R.color.deepSleep, R.color.lightSleep, R.color.remSleep, R.color.vigil};

        PieChartPainter pieChartView = new PieChartPainter(this, colors, values);
        container.addView(pieChartView);

        title.setText(date);
        vigilText.setText(percentageFormat(vigilPercentage) + "%");
        lightText.setText(percentageFormat(lightSleepPercentage) + "%");
        deepText.setText(percentageFormat(deepSleepPercentage) + "%");
        remText.setText(percentageFormat(remSleepPercentage) + "%");

        ChallengesUpdater challengesUpdater = new ChallengesUpdater(connection);
        challengesUpdater.updateChartRecord();

        setTheme();
    }

    private float convertDataToPercentage(float data, float total) {
        if (data == -1) {
            Toast.makeText(this, "SIN DATOS PARA ESTA FECHA", Toast.LENGTH_SHORT).show();
            finish();
        }
        return (data / total) * 100;
    }

    private String percentageFormat(float number) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(number);
    }

    private void setTheme() {
        View view = findViewById(R.id.chartPieVisualizer);
        Themes.setBackgroundColor(this, view);
    }
}