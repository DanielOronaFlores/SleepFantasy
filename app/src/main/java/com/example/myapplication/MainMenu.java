package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import Avatar.AvatarSkins;
import Database.DataAccess.AvatarDataAccess;
import Database.DataAccess.PreferencesDataAccess;
import Database.DatabaseConnection;
import Dialogs.AvatarInformationFragment;
import Services.SleepTracker;
import SortingAlgorithm.SleepEvaluator;

public class MainMenu extends AppCompatActivity {
    private DatabaseConnection connection;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        initializeDatabase();
        AvatarDataAccess avatarDataAccess = new AvatarDataAccess(connection);

        ImageView imgAvatar = findViewById(R.id.avatarDisplay);
        ImageView gameSelectorButton = findViewById(R.id.gameSelectorButton);
        ImageView imgRecordVisualizer = findViewById(R.id.recordVisualizer);
        ImageView chartsVisualizerButton = findViewById(R.id.menuChartsButton);
        ImageView avatarInformationButton = findViewById(R.id.avatarInformationButton);
        ImageView imgMusicSelector = findViewById(R.id.musicSelector);

        AvatarSkins avatarSkins = new AvatarSkins();
        int[] skins = avatarSkins.getAvatarSkins(avatarDataAccess.getCharacterClass());
        imgAvatar.setImageResource(skins[avatarDataAccess.getCharacterPhase() -1]); //TODO: Establecer como apariencia predeterminada la 4.


        imgAvatar.setOnClickListener(view -> {
            Intent intent = new Intent(this, Configuration.class);
            startActivity(intent);
        });
        gameSelectorButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, GameModeSelector.class);
            startActivity(intent);
        });
        imgRecordVisualizer.setOnClickListener(view -> {
            Intent intent = new Intent(this, Recordings.class);
            startActivity(intent);
        });
        chartsVisualizerButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChartSelector.class);
            startActivity(intent);
        });
        imgMusicSelector.setOnClickListener(view -> {
            Intent intent = new Intent(this, PlaylistSelector.class);
            startActivity(intent);
        });
        avatarInformationButton.setOnClickListener(view -> {
            AvatarInformationFragment avatarInformation = new AvatarInformationFragment();
            avatarInformation.show(getSupportFragmentManager(), "Avatar Information");
        });

        PreferencesDataAccess preferencesDataAccess = new PreferencesDataAccess(connection);
        if (preferencesDataAccess.getRecordSnorings()) askRecordingPermission();

        askBodySensorsPermission();

        //Intent intent = new Intent(this, PostureSensor.class);
        //startService(intent);

        //Intent intent = new Intent(this, SleepTracker.class);
        //startService(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onStart() {
        super.onStart();
        initializeDatabase();
    }

    private void initializeDatabase() {
        connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();
    }

    private void askRecordingPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO}, 1);
        }
    }

    private void askBodySensorsPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BODY_SENSORS}, 1);
        }
    }
}