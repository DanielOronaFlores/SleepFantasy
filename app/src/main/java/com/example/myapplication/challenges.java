package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
        displayActiveChallenge();

        //TODO: Placeholder de button (ELIMINAR)
        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> button());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.closeDatabase();
    }

    private void button() { //TODO: Placeholder (ELIMINAR)
        Log.d("INTERFACE", "Boton presionado");
        //Para pruebas
    }

    private void displayActiveChallenge() {
        int currentChallenge = challengesDataAccess.getActiveChallenge();

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(2, Color.parseColor("#FFD700")); // Borde de color dorado
        drawable.setColor(Color.parseColor("#72773EAA")); // Fondo transparente

        if (currentChallenge > 0) {
            challengeTextViews[currentChallenge - 1].setBackground(drawable);
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