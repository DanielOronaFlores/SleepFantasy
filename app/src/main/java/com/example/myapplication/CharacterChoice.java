package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import GameManagers.ExperienceManager;
import Database.DatabaseConnection;

public class CharacterChoice extends AppCompatActivity {
    private final int[] characters = { //TODO: Cambiar por los personajes reales.
            R.drawable.placeholder_cono,
            R.drawable.placeholder_jaime,
            R.drawable.placeholder_bruja,
            R.drawable.placeholder_jirafa,
            R.drawable.placeholder_quick
    };
    private final DatabaseConnection connection = DatabaseConnection.getInstance(this);;
    private final DatabaseConnection.AvatarCreator avatarManager = new DatabaseConnection.AvatarCreator(connection);
    private ImageView currentCharacter, leftButton, rightButton;
    private int idCharacter = 0;
    private String name;
    private byte age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_choice);

        connection.openDatabase(); //TODO: Revisar si es necesario abrir la base de datos en esta actividad.

        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);
        currentCharacter = findViewById(R.id.selectCharacter);

        Intent avatarData = getIntent();
        name = avatarData.getStringExtra("name");
        age = avatarData.getByteExtra("age", (byte) 0);

        leftButton.setOnClickListener(view -> {
            idCharacter = (idCharacter - 1 + characters.length) % characters.length;
            currentCharacter.setImageResource(characters[idCharacter]);
        });
        rightButton.setOnClickListener(view -> {
            idCharacter = (idCharacter + 1) % characters.length;
            currentCharacter.setImageResource(characters[idCharacter]);
        });

        currentCharacter.setOnClickListener(view -> createAvatar(name, age));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.closeDatabase();
    }

    private void finishAvatarCreation() {
        Intent goToMainMenu = new Intent(this, MainMenu.class);
        startActivity(goToMainMenu);
        finish();
    }

    private void createAvatar(String name, byte age) {
        byte avatarClass = (byte) idCharacter;
        byte AVATAR_PHASE_DEFAULT = 4, AVATAR_LEVEL_INITIAL = 1, AVATAR_CURRENT_EXPERIENCE_INITIAL = 0;

        avatarManager.createAvatar(
                name,
                age,
                AVATAR_LEVEL_INITIAL,
                (byte) 0, //TODO: Cambiar por avatarClass cuando se tengan las imagenes.
                AVATAR_CURRENT_EXPERIENCE_INITIAL,
                ExperienceManager.calculateRequiredExperience(AVATAR_LEVEL_INITIAL),
                AVATAR_PHASE_DEFAULT
        );

        finishAvatarCreation();
    }
}