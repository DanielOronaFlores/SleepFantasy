package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import Database.DataAccess.PreferencesDataAccess;
import Database.DataAccess.RewardsDataAccess;
import Database.DatabaseConnection;
import Styles.Themes;

public class ShowReward extends AppCompatActivity {
    private RewardsDataAccess rewardsDataAccess;
    private PreferencesDataAccess preferencesDataAccess;
    private TextView titleReward, descriptionReward;
    private Intent intent;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        rewardsDataAccess = new RewardsDataAccess();
        preferencesDataAccess = new PreferencesDataAccess(DatabaseConnection.getInstance(this));

        titleReward = findViewById(R.id.reward_name);
        descriptionReward = findViewById(R.id.reward_description);

        intent = getIntent();

        int notificationSound = preferencesDataAccess.getNotificationSound();
        if (notificationSound != 0) {
            mediaPlayer = MediaPlayer.create(this, notificationSounds[notificationSound - 1]);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        int rewardId = intent.getIntExtra("rewardId", 0);
        int rewardType = rewardsDataAccess.getRewardType(rewardId);

        String rewardName = getRewardName(rewardType);
        String rewardDescription = getRewardDescription(rewardType);

        titleReward.setText(rewardName);
        descriptionReward.setText(rewardDescription);

        setTheme();

        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    private String getRewardName(int rewardId) {
        switch (rewardId) {
            case 1:
                return "Audio para reproducir";
            case 2:
                return "Interfaz para la app";
            case 3:
                return "Sonido de victoria";
            case 4:
                return "Color para tu avatar";
            default:
                return "Recompensa no encontrada";
        }
    }
    private String getRewardDescription(int rewardId) {
        switch (rewardId) {
            case 1:
                return "Decubre cuál es en la sección de playlists.";
            case 2:
                return "Puedes cambiarlo en la sección de ajustes.";
            case 3:
                return "Establecelo en la sección de ajustes.";
            case 4:
                return "Puedes escogerlo en la sección de ajustes.";
            default:
                return "Recompensa no encontrada";
        }
    }

    private final int[] notificationSounds = {
            R.raw.notification_banana,
            R.raw.notification_dixie,
            R.raw.notification_fanfare,
            R.raw.notification_fantasy,
            R.raw.notification_galaxy,
            R.raw.notification_hidden,
            R.raw.notification_jiggy,
            R.raw.notification_mega,
            R.raw.notification_minecraft,
            R.raw.notification_pokemon,
            R.raw.notification_shine,
            R.raw.notification_sonic,
            R.raw.notification_star,
            R.raw.notification_steam,
            R.raw.notification_xbox
    };

    private void setTheme() {
        Themes.setBackgroundColor(this, findViewById(R.id.rewards));
    }
}