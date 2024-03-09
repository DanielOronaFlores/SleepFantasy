package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Adapters.AdapterMissions;
import Database.DatabaseConnection;
import Database.DataAccess.MissionDataAccess;
import Models.Mission;

public class Missions extends AppCompatActivity {
    private DatabaseConnection connection;
    private MissionDataAccess missionDataAccess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missions);

        connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();
        missionDataAccess = new MissionDataAccess(connection);

        List<Mission> missions = missionDataAccess.getAllMissions();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewMissions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new AdapterMissions(missions, getSupportFragmentManager()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.closeDatabase();
    }
}