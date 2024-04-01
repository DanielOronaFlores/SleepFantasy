package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Adapters.AdapterAudios;
import Adapters.AdapterChallenges;
import Database.DataAccess.ChallengesDataAccess;
import Database.DatabaseConnection;

public class ChallengesList extends AppCompatActivity {
    private DatabaseConnection connection;
    private ChallengesDataAccess challengesDataAccess;
    private RecyclerView recyclerView;
    private AdapterChallenges adapterChallenges;
    private String[] challengesStrings;
    private List<Integer> challenges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges_list);

        connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();
        challengesDataAccess = new ChallengesDataAccess(connection);

        Intent intent = getIntent();
        int challengeId = intent.getIntExtra("challengeId", -1);

        if (challengeId == 1) {
            challenges = challengesDataAccess.getCompletedChallenges();
        } else if (challengeId == 2) {
            challenges = challengesDataAccess.getFailedChallenges();
        } else if (challengeId == 3) {
            challenges = challengesDataAccess.getUnassignedChallenges();
        } else {
            throw new RuntimeException("Invalid challenge ID");
        }

        challengesStrings = getResources().getStringArray(R.array.challenges);
        List<String> finalChallenges = new ArrayList<>();

        if (challenges == null || challenges.isEmpty()) {
            Toast.makeText(this, "NO HAY DESAFIOS PARA MOSTRAR", Toast.LENGTH_SHORT).show();
        } else {
            for (int challenge : challenges) {
                finalChallenges.add(challengesStrings[challenge - 1]);
            }


            adapterChallenges = new AdapterChallenges(finalChallenges);
            recyclerView = findViewById(R.id.recyclerChallenges);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adapterChallenges);
        }
    }
}