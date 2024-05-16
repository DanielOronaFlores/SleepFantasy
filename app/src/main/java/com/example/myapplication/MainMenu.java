package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import Avatar.CharactersList;
import Database.DataAccess.AvatarDataAccess;
import Database.DataAccess.TipsDataAccess;
import Database.DatabaseConnection;
import Dates.DateManager;
import Dialogs.AvatarInformationFragment;
import Dialogs.TipFragment;
import Files.AudiosPaths;
import Files.FilesManager;
import GameManagers.Challenges.ChallengesManager;
import Permissions.Permissions;
import Services.PostureSensor;
import SleepEvaluator.SleepEvaluator;
import SleepEvaluator.Trainer.BayesCreator;
import Styles.Themes;
import Tips.Tips;

public class MainMenu extends AppCompatActivity {
    private AvatarDataAccess avatarDataAccess;
    private TipsDataAccess tipsDataAccess;
    private ImageView imgAvatar, imgGameSelector, imgRecordVisualizer, imgChartsVisualizer, imgAvatarInformation, imgMusicSelector;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        avatarDataAccess = new AvatarDataAccess(DatabaseConnection.getInstance(this));
        tipsDataAccess = new TipsDataAccess(DatabaseConnection.getInstance(this));

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

        int[] skins = CharactersList.getCharacterPhases(avatarDataAccess.getCharacterClass());
        imgAvatar.setImageResource(skins[avatarDataAccess.getCharacterPhase() -1]);

        Permissions.askBodySensorsPermission(this, this);

        if (!tipsDataAccess.isDisplayed()) {
            TipFragment tipFragment = new TipFragment();
            tipFragment.show(getSupportFragmentManager(), "Tip");
        }

        setTheme();
    }

    private void deleteRecordingsFiles() {
        String lastDateModified = FilesManager.getLastDateModified(AudiosPaths.getRecordingsPCMPath());
        lastDateModified = DateManager.convertDate(lastDateModified);

        if (lastDateModified != null && DateManager.hasPassedHoursSince(lastDateModified, 24)) {
            FilesManager.deleteFile(AudiosPaths.getRecordingsPCMPath());
            FilesManager.deleteFile(AudiosPaths.getRecordings3GPPath());
            FilesManager.deleteFile(AudiosPaths.getListSoundsPath());
        }
    }

    private void setTheme() {
        Themes.setBackgroundColor(this, findViewById(R.id.mainMenu));
    }
}