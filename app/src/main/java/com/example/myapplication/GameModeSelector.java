package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class GameModeSelector extends AppCompatActivity {

    private Button btMissions, btChallenges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);

        btMissions = findViewById(R.id.missions);
        btMissions.setOnClickListener(view -> {
            Intent intent = new Intent(this, missions.class);
            startActivity(intent);
        });

        btChallenges = findViewById(R.id.challenges);
        btChallenges.setOnClickListener(view -> {
            Intent intent = new Intent(this, challenges.class);
            startActivity(intent);
        });
    }
}