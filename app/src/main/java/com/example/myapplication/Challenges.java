package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import Database.DataAccess.ChallengesDataAccess;
import Database.DatabaseConnection;
import Styles.Themes;

public class Challenges extends AppCompatActivity {
    private TextView currentActiveChallenge;
    private Button completedChallenges, failedChallenges, unassignedChallenges;
    private ChallengesDataAccess challengesDataAccess;
    private DatabaseConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        connection = DatabaseConnection.getInstance(this);
        challengesDataAccess = new ChallengesDataAccess(connection);
        currentActiveChallenge = findViewById(R.id.activeChallenge);

        completedChallenges = findViewById(R.id.completedChallenges);
        failedChallenges = findViewById(R.id.failedChallenges);
        unassignedChallenges = findViewById(R.id.unassignedChallenges);

        completedChallenges.setOnClickListener(v -> goToChallengeDetails(1));
        failedChallenges.setOnClickListener(v -> goToChallengeDetails(2));
        unassignedChallenges.setOnClickListener(v -> goToChallengeDetails(3));
    }

    @Override
    protected void onStart() {
        super.onStart();

        int activeChallenge = challengesDataAccess.getActiveChallenge();
        System.out.println("Active Challenge: " + activeChallenge);
        String[] challenges = getResources().getStringArray(R.array.challenges);
        currentActiveChallenge.setText(challenges[activeChallenge -1]); // Se resta 1 porque los índices empiezan en 0

        if (challengesDataAccess.isCompleted(activeChallenge)) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setColor(Color.parseColor("#72773EAA")); // Fondo transparente
            drawable.setStroke(2, Color.parseColor("#FFD700")); // Border dorado
            currentActiveChallenge.setBackground(drawable);
        }

        setTheme();
    }

    private void goToChallengeDetails(int id) {
        Intent intent = new Intent(this, ChallengesList.class);
        intent.putExtra("challengeId", id);
        startActivity(intent);
    }

    private void setTheme() {
        View view = findViewById(R.id.challenges);

        Themes.setBackgroundColor(this, view);
        Themes.setButtonTheme(this, completedChallenges);
        Themes.setButtonTheme(this, failedChallenges);
        Themes.setButtonTheme(this, unassignedChallenges);
        Themes.setChallengeTextViewTheme(this, currentActiveChallenge);
    }
}