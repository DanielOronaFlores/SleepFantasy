package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import Avatar.CharactersList;
import Database.DataUpdates.AvatarCreator;
import GameManagers.ExperienceManager;
import Database.DatabaseConnection;
import SleepEvaluator.Trainer.BayesCreator;
import Styles.Themes;

public class CharacterChoice extends AppCompatActivity {
    private DatabaseConnection connection;
    private AvatarCreator avatarManager;
    private ImageView currentCharacter, leftButton, rightButton;
    private int[] characters;
    private int idCharacter = 0;
    private String name;
    private byte age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_choice);

        connection = DatabaseConnection.getInstance(this);
        characters = CharactersList.getCharactersList();

        avatarManager = new AvatarCreator(connection);

        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);
        currentCharacter = findViewById(R.id.selectCharacter);

        Intent avatarData = getIntent();
        name = avatarData.getStringExtra("name");
        age = avatarData.getByteExtra("age", (byte) 0);

        currentCharacter.setOnClickListener(view -> {
            createProbabilities();
            createAvatar(name, age);
        });

        leftButton.setOnClickListener(view -> {
            idCharacter = (idCharacter - 1 + characters.length) % characters.length;
            currentCharacter.setImageResource(characters[idCharacter]);
        });
        rightButton.setOnClickListener(view -> {
            idCharacter = (idCharacter + 1) % characters.length;
            currentCharacter.setImageResource(characters[idCharacter]);
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        connection.openDatabase();

        currentCharacter.setImageResource(characters[idCharacter]);
        setTheme();
    }

    private void goToMainMenu() {
        Intent goToMainMenu = new Intent(this, MainMenu.class);
        startActivity(goToMainMenu);
        finish();
    }

    private void createAvatar(String name, byte age) {
        byte avatarClass = (byte) (idCharacter + 1);
        byte AVATAR_PHASE_DEFAULT = 4,
                AVATAR_LEVEL_INITIAL = 1,
                AVATAR_CURRENT_EXPERIENCE_INITIAL = 0;

        avatarManager.createAvatar(
                name,
                age,
                AVATAR_LEVEL_INITIAL,
                avatarClass,
                AVATAR_CURRENT_EXPERIENCE_INITIAL,
                ExperienceManager.calculateRequiredExperience(AVATAR_LEVEL_INITIAL),
                AVATAR_PHASE_DEFAULT
        );

        goToMainMenu();
    }

    private void createProbabilities() {
        BayesCreator bayes = new BayesCreator();
        bayes.createProbabilities();
    }

    private void setTheme() {
        View view = findViewById(R.id.characterChoice);
        Themes.setBackgroundColor(this, view);
    }
}