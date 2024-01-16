package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.TextView;

import DataAccess.ChallengesDataAccess;
import Database.DatabaseConnection;
import GameManagers.ChallengesManager;

public class challenges extends AppCompatActivity {
    private TextView[] challengeTextViews;
    private ChallengesDataAccess challengesDataAccess;
    private DatabaseConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();

        challengesDataAccess = new ChallengesDataAccess(connection);
        ChallengesManager challengesManager = new ChallengesManager();

        Intent intent = new Intent(this, challengesManager.getClass());
        startService(intent);

        initializeChallengeTextViews();
        setStrokeColors();

    }

    private void setStrokeColors() {
        for (int i = 0; i < challengeTextViews.length; i++) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setColor(Color.parseColor("#72773EAA")); // Fondo transparente

            int currentChallenge = challengesDataAccess.getActiveChallenge();

            if (i == currentChallenge - 1) {
                // Configura el borde de color dorado para el reto activo
                drawable.setStroke(2, Color.parseColor("#FFD700"));
            } else {
                // Si el reto está completado, el borde es verde; si no, rojo
                if (challengesDataAccess.isCompleted(i + 1)) {
                    drawable.setStroke(2, Color.parseColor("#119000")); // Verde
                } else if (!challengesDataAccess.isCompleted(i + 1) && challengesDataAccess.isDisplayed(i + 1)) {
                    drawable.setStroke(2, Color.parseColor("#950505")); // Rojo
                }
            }

            // Establece el fondo y borde para la vista de texto actual
            challengeTextViews[i].setBackground(drawable);
        }
    }


    private void initializeChallengeTextViews() {
        challengeTextViews = new TextView[15]; // Número total de TextViews

        for (int i = 0; i < challengeTextViews.length; i++) {
            int resourceId = getResources().getIdentifier("challenge" + (i + 1), "id", getPackageName());
            challengeTextViews[i] = findViewById(resourceId);
        }
    }
}