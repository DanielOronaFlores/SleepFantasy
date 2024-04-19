package com.example.myapplication;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import Database.DataAccess.PreferencesDataAccess;
import Database.DataAccess.RewardsDataAccess;
import Database.DataUpdates.PreferencesDataUpdate;
import Database.DatabaseConnection;
import Styles.Themes;

public class ThemeSelector extends AppCompatActivity {
    private PreferencesDataUpdate preferencesDataUpdate;
    private RewardsDataAccess rewardsDataAccess;
    private ImageView theme1, theme2, theme3, theme4, theme5, theme6;
    private List<ImageView> themes;
    private List<Boolean> given;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_selector);

        preferencesDataUpdate = new PreferencesDataUpdate(DatabaseConnection.getInstance(this));
        rewardsDataAccess = new RewardsDataAccess(DatabaseConnection.getInstance(this));

        theme1 = findViewById(R.id.theme1);
        theme2 = findViewById(R.id.theme2);
        theme3 = findViewById(R.id.theme3);
        theme4 = findViewById(R.id.theme4);
        theme5 = findViewById(R.id.theme5);
        theme6 = findViewById(R.id.theme6);

        theme1.setOnClickListener(v -> changeTheme(0));
        theme2.setOnClickListener(v -> changeTheme(1));
        theme3.setOnClickListener(v -> changeTheme(2));
        theme4.setOnClickListener(v -> changeTheme(3));
        theme5.setOnClickListener(v -> changeTheme(4));
        theme6.setOnClickListener(v -> changeTheme(5));

        themes = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            themes.add(findViewById(getResources().getIdentifier("theme" + (i + 1), "id", getPackageName())));
        }

        given = rewardsDataAccess.getGivenPerType(2);
    }
    @Override
    protected void onStart(){
        super.onStart();

        for (int i = 0; i < given.size(); i++) {
            if (!given.get(i)) {
                ImageView aux = themes.get(i);
                aux.setImageResource(R.drawable.rewards_lock);
            }
        }

        Themes.setBackgroundColor(this, findViewById(R.id.ThemeSelector));
    }

    private void changeTheme(int theme) {
        if (theme == 0 || given.get(theme -1)) {
            preferencesDataUpdate.setTheme(theme);
            Themes.setBackgroundColor(this, findViewById(R.id.ThemeSelector));
            recreate();
        }
    }
}