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
    private ImageView imgAvatar, imgSelector;
    private TextView txUserData, txAvatarData, txExperience;
    private DatabaseConnection connection;
    private AvatarDataAccess avatarDataAccess;
    private AvatarSkins avatarSkins;
    private NameClasses nameClasses;
    private int[] skins;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();
        avatarDataAccess = new AvatarDataAccess(connection);

        imgAvatar = findViewById(R.id.avatarDisplay);
        imgSelector = findViewById(R.id.gameSelector);

        txUserData = findViewById(R.id.userData);
        txAvatarData = findViewById(R.id.avatarData);
        txExperience = findViewById(R.id.avatarExperience);

        avatarSkins = new AvatarSkins();
        skins = avatarSkins.getAvatarSkins(avatarDataAccess.getCharacterClass());
        imgAvatar.setImageResource(skins[avatarDataAccess.getCharacterPhase() -1]); //TODO: Establecer como apariencia predeterminada la 4.

        txUserData.setText(avatarDataAccess.getAvatarName() + " | " + avatarDataAccess.getAvatarAge());

        nameClasses = new NameClasses();
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.closeDatabase();
    }
}