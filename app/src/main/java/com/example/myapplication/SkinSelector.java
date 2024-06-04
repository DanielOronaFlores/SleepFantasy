package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import Database.DataAccess.PreferencesDataAccess;
import Database.DataAccess.RewardsDataAccess;
import Database.DataUpdates.PreferencesDataUpdate;
import Database.DatabaseConnection;
import GameManagers.Challenges.ChallengesUpdater;
import Styles.Themes;

public class SkinSelector extends AppCompatActivity {
    private PreferencesDataUpdate preferencesDataUpdate;
    private RewardsDataAccess rewardsDataAccess;
    private ChallengesUpdater challengesUpdater;
    private List<ImageView> skins;
    private List<Boolean> given;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_selector);

        preferencesDataUpdate = new PreferencesDataUpdate(DatabaseConnection.getInstance(this));
        rewardsDataAccess = new RewardsDataAccess();
        challengesUpdater = new ChallengesUpdater();

        given = rewardsDataAccess.getGivenPerType(4);
        skins = new ArrayList<>();
        initializeImageViews();
        setSkinOnClickListeners();
    }
    @Override
    public void onStart(){
        super.onStart();

        Themes.setBackgroundColor(this, findViewById(R.id.SkinSelector));
        for (int i = 1; i < skins.size(); i++) {
            if (!given.get(i - 1)) {
                ImageView aux = skins.get(i);
                aux.setImageResource(R.drawable.rewards_lock);
            }
        }
    }

    private void initializeImageViews() {
        for (int i = 0; i <= 10; i++) {
            int resID = getResources().getIdentifier("skin" + i, "id", getPackageName());
            ImageView imageView = findViewById(resID);
            skins.add(imageView);
        }
    }
    private void setSkinOnClickListeners() {
        for (int i = 0; i <= 10; i++) {
            final int skinIndex = i;
            skins.get(i).setOnClickListener(v -> setAvatarSkin(skinIndex));
        }
    }

    private void setAvatarSkin(int skinID ) {
        if (skinID == 0 || given.get(skinID -1)) {
            System.out.println("setAvatarSkin: " + skinID);
            preferencesDataUpdate.setAvatarSkin(skinID);
            Toast.makeText(this, "SE HA CAMBIADO EL ASPECTO DEL AVATAR", Toast.LENGTH_SHORT).show();
            challengesUpdater.updateAvatarVisualRecord();
            finish();
        }
    }
}