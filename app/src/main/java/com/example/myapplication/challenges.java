package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import Database.Challenges.ChallengesDataAccess;
import Database.Challenges.ChallengesDataUpdate;
import Database.DatabaseConnection;
import GameManagers.ChallengesManager;

public class challenges extends AppCompatActivity {
    private Button button;
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

        button = findViewById(R.id.button);
        button.setOnClickListener(v -> button());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.closeDatabase();
    }

    private void button() {
        Log.d("INTERFACE", "Boton presionado");
        challengesManager.selectNewChallenge();
    }
}