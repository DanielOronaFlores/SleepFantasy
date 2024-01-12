package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import GameManagers.ExperienceManager;
import Database.DatabaseConnection;
import DataAccess.MissionDataAccess;
import DataUpdates.MissionDataUpdate;
import GameManagers.MissionsManager;

public class missions extends AppCompatActivity {
    private final ImageView[] missions = new ImageView[20];
    private DatabaseConnection connection;
    private MissionDataAccess missionDataAccess;

    //TODO: PLACEHOLDER
    private TextView placeholder;
    private MissionsManager missionsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missions);

        connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();
        missionDataAccess = new MissionDataAccess(connection);

        initializeViews();
        setMissionClickListeners();

        //TODO: PLACEHOLDER
        placeholder = findViewById(R.id.title);
        missionsManager = new MissionsManager();
        placeholder.setOnClickListener(view ->
            placeholderFunction()
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.closeDatabase();
    }

    private void placeholderFunction() {
        int quantity = missionDataAccess.getCurrentQuantity(1) + 1;
        missionsManager.updateMission(1, quantity);
    }

    private void initializeViews() {
        for (int i = 0; i < missions.length; i++) {
            int resourceId = getResources().getIdentifier("mission" + (i + 1), "id", getPackageName());
            missions[i] = findViewById(resourceId);

            int difficult = missionDataAccess.getCurrentDifficult(i + 1);
            switch (difficult) {
                case 1:
                    missions[i].setImageResource(R.drawable.medals_noone);
                    break;
                case 2:
                    missions[i].setImageResource(R.drawable.medals_bronze);
                    break;
                case 3:
                    missions[i].setImageResource(R.drawable.medals_silver);
                    break;
                case 4:
                    missions[i].setImageResource(R.drawable.medals_gold);
                    break;
            }
        }
    }
    private void setMissionClickListeners() {
        for (int i = 0; i < missions.length; i++) {
            int finalI = i + 1;
            missions[i].setOnClickListener(view -> openMissionDescription(finalI));
        }
    }
    private void openMissionDescription(int missionID) {
        Mission_Description fragment = new Mission_Description();
        Bundle args = new Bundle();
        args.putInt("missionId", missionID);
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.missionFragment, fragment)
                .commit();
    }
}