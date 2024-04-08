package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import Styles.Themes;

public class GameModeSelector extends AppCompatActivity {

    private Button btMissions, btChallenges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);

        btMissions = findViewById(R.id.missions);
        btMissions.setOnClickListener(view -> {
            Intent intent = new Intent(this, Missions.class);
            startActivity(intent);
        });

        btChallenges = findViewById(R.id.challenges);
        btChallenges.setOnClickListener(view -> {
            Intent intent = new Intent(this, Challenges.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        setTheme();
    }

    private void setTheme() {
        Themes.setBackgroundColor(this, findViewById(R.id.selector));
        Themes.setButtonTheme(this, btMissions);
        Themes.setButtonTheme(this, btChallenges);
    }
}