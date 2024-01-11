package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import Database.Challenges.ChallengesDataAccess;
import Database.Challenges.ChallengesDataUpdate;
import Database.DatabaseConnection;
import GameManagers.ChallengesManager;

public class challenges extends AppCompatActivity {
    private Button button; //TODO: Placeholder
    private TextView[] challengeTextViews;
    private ChallengesDataAccess challengesDataAccess;
    private ChallengesDataUpdate challengesDataUpdate;
    private ChallengesManager challengesManager;
    private DatabaseConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();
        challengesDataAccess = new ChallengesDataAccess(connection);
        challengesDataUpdate = new ChallengesDataUpdate(connection);
        challengesManager = new ChallengesManager(challengesDataAccess, challengesDataUpdate);

        //TODO: Placeholder
        button = findViewById(R.id.button);
        button.setOnClickListener(v -> button());

        initializeChallengeTextViews();
        displayActiveChallenge();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.closeDatabase();
    }

    private void button() { //TODO: Placeholder
        Log.d("INTERFACE", "Boton presionado");
        challengesManager.selectNewChallenge();
    }

    private void displayActiveChallenge() {
        int currentChallenge = challengesDataAccess.getActiveChallenge();

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(2, Color.parseColor("#FFD700")); // Borde de color dorado
        drawable.setColor(Color.parseColor("#72773EAA")); // Fondo transparente

        challengeTextViews[currentChallenge - 1].setBackground(drawable);
    }

    private void initializeChallengeTextViews() {
        challengeTextViews = new TextView[15]; // Número total de TextViews

        for (int i = 0; i < challengeTextViews.length; i++) {
            int resourceId = getResources().getIdentifier("challenge" + (i + 1), "id", getPackageName());
            challengeTextViews[i] = findViewById(resourceId);
        }
    }
}