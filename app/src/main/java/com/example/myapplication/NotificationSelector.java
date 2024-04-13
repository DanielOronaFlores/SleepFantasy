package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import Database.DataAccess.PreferencesDataAccess;
import Database.DataAccess.RewardsDataAccess;
import Database.DatabaseConnection;
import Styles.Themes;

public class NotificationSelector extends AppCompatActivity {
    private PreferencesDataAccess preferencesDataAccess;
    private RewardsDataAccess rewardsDataAccess;
    private List<ImageView> sounds;
    private List<Boolean> given;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_selector);

        preferencesDataAccess = new PreferencesDataAccess(DatabaseConnection.getInstance(this));
        rewardsDataAccess = new RewardsDataAccess(DatabaseConnection.getInstance(this));

        given = rewardsDataAccess.getGivenPerType(3);
        sounds = new ArrayList<>();
        initializeImageViews();
        setNotificationOnClickListeners();
    }
    @Override
    public void onStart(){
        super.onStart();

        Themes.setBackgroundColor(this, findViewById(R.id.NotificationSelector));
        for (int i = 0; i < sounds.size(); i++) {
            if (!given.get(i)) {
                ImageView aux = sounds.get(i);
                aux.setImageResource(R.drawable.rewards_lock);
            }
        }
    }

    private void initializeImageViews() {
        for (int i = 1; i <= 15; i++) {
            System.out.println("notification" + i);
            int resID = getResources().getIdentifier("notification" + i, "id", getPackageName());
            ImageView imageView = findViewById(resID);
            sounds.add(imageView);
        }
    }
    private void setNotificationOnClickListeners() {
        for (int i = 0; i < 15; i++) {
            final int notificationIndex = i;
            sounds.get(i).setOnClickListener(v -> setNotificationImage(notificationIndex));
        }
    }
    private void setNotificationImage(int notificationID ) {
        if (given.get(notificationID)) {
            System.out.println("Notification: " + notificationID);
            preferencesDataAccess.setNotificationSound(notificationID);
            Toast.makeText(this, "SE HA CAMBIADO EL SONIDO DE NOTIFICACION", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}