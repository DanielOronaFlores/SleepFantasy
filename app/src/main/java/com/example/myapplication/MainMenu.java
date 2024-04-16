package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import Avatar.CharactersList;
import Database.DataAccess.AvatarDataAccess;
import Database.DatabaseConnection;
import Dates.DateManager;
import Dialogs.AvatarInformationFragment;
import Files.AudiosPaths;
import Files.FilesManager;
import GameManagers.Challenges.ChallengesManager;
import Permissions.Permissions;
import Services.PostureSensor;
import Styles.Themes;

public class MainMenu extends AppCompatActivity {
    private DatabaseConnection connection;
    private AvatarDataAccess avatarDataAccess;
    private ImageView imgAvatar, imgGameSelector, imgRecordVisualizer, imgChartsVisualizer, imgAvatarInformation, imgMusicSelector;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        connection = DatabaseConnection.getInstance(this);
        avatarDataAccess = new AvatarDataAccess(connection);

        imgAvatar = findViewById(R.id.avatarDisplay);
        imgGameSelector = findViewById(R.id.gameSelectorButton);
        imgRecordVisualizer = findViewById(R.id.recordVisualizer);
        imgChartsVisualizer = findViewById(R.id.menuChartsButton);
        imgAvatarInformation = findViewById(R.id.avatarInformationButton);
        imgMusicSelector = findViewById(R.id.musicSelector);

        imgAvatar.setOnClickListener(view -> {
            Intent intent = new Intent(this, Configuration.class);
            startActivity(intent);
        });
        imgGameSelector.setOnClickListener(view -> {
            Intent intent = new Intent(this, GameModeSelector.class);
            startActivity(intent);
        });
        imgRecordVisualizer.setOnClickListener(view -> {
            Intent intent = new Intent(this, Recordings.class);
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
            AvatarInformationFragment avatarInformation = new AvatarInformationFragment();
            avatarInformation.show(getSupportFragmentManager(), "Avatar Information");
        });

        Intent intent = new Intent(this, PostureSensor.class);
        startService(intent);

        ChallengesManager challengesManager = new ChallengesManager();
        challengesManager.update();

        deleteRecordingsFiles();
    }
    @Override
    protected void onStart() {
        super.onStart();
        connection.openDatabase();

        int[] skins = CharactersList.getCharacterPhases(avatarDataAccess.getCharacterClass());
        imgAvatar.setImageResource(skins[avatarDataAccess.getCharacterPhase() -1]);

        Permissions.askBodySensorsPermission(this, this);

        setTheme();
    }

    private void deleteRecordingsFiles() {
        DateManager dateManager = new DateManager();
        AudiosPaths audiosPaths = new AudiosPaths();

        String lastDateModified = FilesManager.getLastDateModified(audiosPaths.getRecordingsPCMPath());
        lastDateModified = dateManager.convertDate(lastDateModified);

        if (lastDateModified != null && dateManager.havePassed24Hours(lastDateModified)) {
            FilesManager.deleteFiles(audiosPaths.getRecordingsPCMPath());
            FilesManager.deleteFiles(audiosPaths.getRecordings3GPPath());
            FilesManager.deleteFiles(audiosPaths.getListSoundsPath());
        }
    }

    private void setTheme() {
        View view = findViewById(R.id.mainMenu);
        Themes.setBackgroundColor(this, view);
    }
}