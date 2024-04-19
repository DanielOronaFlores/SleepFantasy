package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Database.DataAccess.AvatarDataAccess;
import Database.DataAccess.PreferencesDataAccess;
import Database.DataUpdates.AvatarCreator;
import Database.DataUpdates.AvatarDataUpdate;
import Database.DataUpdates.PreferencesDataUpdate;
import Database.DatabaseConnection;
import Permissions.Permissions;
import Styles.Themes;

public class Configuration extends AppCompatActivity {
    private AvatarCreator avatarManager;
    private AvatarDataUpdate avatarDataUpdate;
    private AvatarDataAccess avatarDataAccess;
    private PreferencesDataAccess preferencesDataAccess;
    private PreferencesDataUpdate preferencesDataUpdate;
    private EditText editTextName, editTextAge;
    private CheckBox checkBoxSaveRecordings, checkBoxRecordAudios;
    private Button buttonChangeAudioQuality, buttonSavePreferences, buttonChangeTheme, buttonChangeAvatarSkin, buttonChangeNotificationSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        avatarDataAccess = new Database.DataAccess.AvatarDataAccess(DatabaseConnection.getInstance(this));
        avatarManager = new AvatarCreator(DatabaseConnection.getInstance(this));
        avatarDataUpdate = new Database.DataUpdates.AvatarDataUpdate(DatabaseConnection.getInstance(this));
        preferencesDataAccess = new PreferencesDataAccess(DatabaseConnection.getInstance(this));
        preferencesDataUpdate = new PreferencesDataUpdate(DatabaseConnection.getInstance(this));

        editTextName = findViewById(R.id.name);
        editTextAge = findViewById(R.id.age);

        checkBoxRecordAudios = findViewById(R.id.recordSnoring);
        checkBoxSaveRecordings = findViewById(R.id.saveAudios);

        buttonSavePreferences = findViewById(R.id.savePreferences);
        buttonChangeAudioQuality = findViewById(R.id.btQuality);
        buttonChangeTheme = findViewById(R.id.changeTheme);
        buttonChangeAvatarSkin = findViewById(R.id.changeAvatarSkin);
        buttonChangeNotificationSound = findViewById(R.id.changeNotification);

        checkBoxRecordAudios.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) Permissions.askRecordingPermission(this, this);
        });
        checkBoxSaveRecordings.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) Permissions.askStoragePermission(this, this);
        });

        buttonSavePreferences.setOnClickListener(view ->
                setUserData(editTextName.getText().toString(), Byte.parseByte(editTextAge.getText().toString()))
        );
        buttonChangeAudioQuality.setOnClickListener(view -> updateQuality());
        buttonChangeTheme.setOnClickListener(view -> {
            Intent intent = new Intent(this, ThemeSelector.class);
            startActivity(intent);
        });
        buttonChangeAvatarSkin.setOnClickListener(view -> {
            if (avatarManager.isAvatarCreated()) {
                Intent intent = new Intent(this, SkinSelector.class);
                startActivity(intent);
            }
        });
        buttonChangeNotificationSound.setOnClickListener(view -> {
            Intent intent = new Intent(this, NotificationSelector.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(avatarManager.isAvatarCreated()) {
            editTextName.setText(avatarDataAccess.getAvatarName());
            editTextAge.setText(String.valueOf(avatarDataAccess.getAvatarAge()));

            checkBoxRecordAudios.setChecked(preferencesDataAccess.getRecordAudios());
            checkBoxSaveRecordings.setChecked(preferencesDataAccess.getSaveRecordings());
        }

        setAudioQualityText();
        setTheme();
    }

    private void setUserData(String name, byte age) {
        if (!name.isEmpty() && age > 0) {
            preferencesDataUpdate.updatePreferences(checkBoxSaveRecordings.isChecked(), checkBoxRecordAudios.isChecked());
            if (avatarManager.isAvatarCreated()) {
                avatarDataUpdate.updateNameAndAge(name, age);
                finish();
                goToMainMenu();
            } else {
                goToCharacterChoice(name, age);
            }
            Toast.makeText(this, "PREFERENCIAS GUARDADAS", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "DATOS INGRESADOS NO VALIDOS", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateQuality() {
        boolean audioQuality = preferencesDataAccess.getAudioQuality();
        preferencesDataUpdate.updateAudioQuality(!audioQuality);

        String quality = audioQuality ? "BAJA" : "ALTA";
        Toast toast = Toast.makeText(this, "CALIDAD DE AUDIO ACTUALIZADA A " + quality, Toast.LENGTH_SHORT);
        toast.show();

        setAudioQualityText();
    }

    private void goToMainMenu() {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }
    private void goToCharacterChoice(String name, byte age) {
        Intent intent = new Intent(this, CharacterChoice.class);
        intent.putExtra("name", name);
        intent.putExtra("age", age);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    private void setAudioQualityText() {
        if (preferencesDataAccess.getAudioQuality()) buttonChangeAudioQuality.setText("ALTA");
        else buttonChangeAudioQuality.setText("BAJA");
    }
    private void setTheme() {
        Themes.setBackgroundColor(this, findViewById(R.id.configuration));
        Themes.setButtonTheme(this, buttonSavePreferences);
        Themes.setButtonTheme(this, buttonChangeTheme);
        Themes.setButtonTheme(this, buttonChangeAvatarSkin);
        Themes.setButtonDataTheme(this, buttonChangeAudioQuality);
    }
}