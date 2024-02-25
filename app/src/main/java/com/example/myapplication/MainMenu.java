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
import DataAccess.AvatarDataAccess;
import DataAccess.PreferencesDataAccess;
import Database.DatabaseConnection;
import Dialogs.AvatarInformation;

public class MainMenu extends AppCompatActivity {
    private final DatabaseConnection connection =  DatabaseConnection.getInstance(this);

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        connection.openDatabase();
        AvatarDataAccess avatarDataAccess = new AvatarDataAccess(connection);

        ImageView imgAvatar = findViewById(R.id.avatarDisplay);
        ImageView imgSelector = findViewById(R.id.gameSelector);
        ImageView imgRecordVisualizer = findViewById(R.id.recordVisualizer);
        ImageView imgChartsVisualizer = findViewById(R.id.chartsVisualizer);
        ImageView imgAvatarInformation = findViewById(R.id.avatarInformation);
        ImageView imgMusicSelector = findViewById(R.id.musicSelector);

        AvatarSkins avatarSkins = new AvatarSkins();
        int[] skins = avatarSkins.getAvatarSkins(avatarDataAccess.getCharacterClass());
        imgAvatar.setImageResource(skins[avatarDataAccess.getCharacterPhase() -1]); //TODO: Establecer como apariencia predeterminada la 4.


        imgAvatar.setOnClickListener(view -> {
            Intent intent = new Intent(this, Configuration.class);
            startActivity(intent);
        });
        imgSelector.setOnClickListener(view -> {
            Intent intent = new Intent(this, GameModeSelector.class);
            startActivity(intent);
        });
        imgRecordVisualizer.setOnClickListener(view -> {
            Intent intent = new Intent(this, recordings.class);
            startActivity(intent);
        });
        imgChartsVisualizer.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChartSelector.class);
            startActivity(intent);
        });
        imgMusicSelector.setOnClickListener(view -> {
            Intent intent = new Intent(this, PlaylistSelector.class);
            startActivity(intent);
        });
        imgAvatarInformation.setOnClickListener(view -> {
            AvatarInformation avatarInformation = new AvatarInformation();
            avatarInformation.show(getSupportFragmentManager(), "Avatar Information");
        });

        PreferencesDataAccess preferencesDataAccess = new PreferencesDataAccess(connection);
        if (preferencesDataAccess.getRecordSnorings()) askRecordingPermission();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.closeDatabase();
    }

    private void askRecordingPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO}, 1);
        }
    }
}