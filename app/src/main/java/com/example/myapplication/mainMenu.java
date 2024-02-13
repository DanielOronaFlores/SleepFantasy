package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import Avatar.AvatarSkins;
import Avatar.NameClasses;
import DataAccess.AvatarDataAccess;
import Database.DatabaseConnection;

public class mainMenu extends AppCompatActivity {
    private DatabaseConnection connection;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();
        AvatarDataAccess avatarDataAccess = new AvatarDataAccess(connection);

        ImageView imgAvatar = findViewById(R.id.avatarDisplay);
        ImageView imgSelector = findViewById(R.id.gameSelector);
        ImageView imgRecordVisualizer = findViewById(R.id.recordVisualizer);
        ImageView imgChartsVisualizer = findViewById(R.id.chartsVisualizer);

        TextView txUserData = findViewById(R.id.userData);
        TextView txAvatarData = findViewById(R.id.avatarData);
        TextView txExperience = findViewById(R.id.avatarExperience);

        AvatarSkins avatarSkins = new AvatarSkins();
        int[] skins = avatarSkins.getAvatarSkins(avatarDataAccess.getCharacterClass());
        imgAvatar.setImageResource(skins[avatarDataAccess.getCharacterPhase() -1]); //TODO: Establecer como apariencia predeterminada la 4.

        txUserData.setText(avatarDataAccess.getAvatarName() + " | " + avatarDataAccess.getAvatarAge());

        NameClasses nameClasses = new NameClasses();
        txAvatarData.setText(
                nameClasses.getNameClass(avatarDataAccess.getCharacterClass()) + " - Level " +
                String.valueOf(avatarDataAccess.getLevel())); //TODO: Agregar el nombre de la clase del avatar.
        txExperience.setText(avatarDataAccess.getCurrentExperience() + "/" + avatarDataAccess.getRequiredExperience());

        imgAvatar.setOnClickListener(view -> {
            Intent intent = new Intent(this, configuration.class);
            startActivity(intent);
        });
        imgSelector.setOnClickListener(view -> {
            Intent intent = new Intent(this, selector.class);
            startActivity(intent);
        });
        imgRecordVisualizer.setOnClickListener(view -> {
            Intent intent = new Intent(this, recordsVisualizer.class);
            startActivity(intent);
        });
        imgChartsVisualizer.setOnClickListener(view -> {
            Intent intent = new Intent(this, chartSelector.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.closeDatabase();
    }
}