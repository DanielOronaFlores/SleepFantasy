package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import Avatar.SystemExperience;
import Database.Avatar.AvatarManager;
import Database.DatabaseConnection;

public class characterChoice extends AppCompatActivity {
    private ImageView left, right, currentCharacter;
    private int[] characters = { //TODO: Cambiar por los personajes reales.
            R.drawable.placeholder_cono,
            R.drawable.placeholder_jaime,
            R.drawable.placeholder_bruja,
            R.drawable.placeholder_jirafa,
            R.drawable.placeholder_quick
    };
    private int idCharacter = 0;
    DatabaseConnection connection;
    private AvatarManager avatarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_choice);

        connection = DatabaseConnection.getInstance(this);
        avatarManager  = new AvatarManager(connection);

        left = findViewById(R.id.leftButton);
        right = findViewById(R.id.rightButton);
        currentCharacter = findViewById(R.id.selectCharacter);

        Intent getAvatarData = getIntent();
        String name = getAvatarData.getStringExtra("name");
        byte age = getAvatarData.getByteExtra("age", (byte) 0);

        left.setOnClickListener(view -> {
            idCharacter = (idCharacter - 1 + characters.length) % characters.length;
            currentCharacter.setImageResource(characters[idCharacter]);
        });
        right.setOnClickListener(view -> {
            idCharacter = (idCharacter + 1) % characters.length;
            currentCharacter.setImageResource(characters[idCharacter]);
        });

        currentCharacter.setOnClickListener(view -> createAvatar(name, age));
    }

    private void createAvatar(String name, byte age) {
        byte avatarClass = generateAvatarClass(idCharacter);

        connection.openDatabase();
        avatarManager.createAvatar(
                name,
                age,
                (byte) 1,
                avatarClass,
                0,
                SystemExperience.calculateRequiredExperience((byte)1),
                (byte)4
        );
        connection.closeDatabase();

        Intent goToMainMenu = new Intent(this, mainMenu.class);
        startActivity(goToMainMenu);
    }
    private byte generateAvatarClass(int idCharacter)
    {
        switch (idCharacter) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            default:
                return -1;
        }
    }
}