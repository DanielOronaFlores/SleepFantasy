package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class missions extends AppCompatActivity {
    private ImageView[] missions = new ImageView[20];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missions);

        initializeViews();
        setMissionClickListeners();
    }
    private void initializeViews() {
        for (int i = 0; i < missions.length; i++) {
            int resourceId = getResources().getIdentifier("mission" + (i + 1), "id", getPackageName());
            missions[i] = findViewById(resourceId);
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