package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import Database.DataAccess.AvatarDataAccess;
import Database.DataAccess.PreferencesDataAccess;
import Database.DataUpdates.AvatarCreator;
import Database.DataUpdates.AvatarDataUpdate;
import Database.DatabaseConnection;
import Permissions.Permissions;
import Styles.Themes;

public class Configuration extends AppCompatActivity {
    private DatabaseConnection connection;
    private AvatarCreator avatarManager;
    private AvatarDataUpdate avatarDataUpdate;
    private AvatarDataAccess avatarDataAccess;
    private PreferencesDataAccess preferencesDataAccess;
    private EditText editTextName, editTextAge;
    private CheckBox checkBoxSaveRecordings, checkBoxRecordAudios;
    private Button buttonChangeAudioQuality, buttonSavePreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        connection = DatabaseConnection.getInstance(this);

        avatarDataAccess = new Database.DataAccess.AvatarDataAccess(connection);
        avatarManager = new AvatarCreator(connection);
        avatarDataUpdate = new Database.DataUpdates.AvatarDataUpdate(connection);
        preferencesDataAccess = new PreferencesDataAccess(connection);

        editTextName = findViewById(R.id.name);
        editTextAge = findViewById(R.id.age);
        checkBoxRecordAudios = findViewById(R.id.recordSnoring);
        checkBoxSaveRecordings = findViewById(R.id.saveAudios);
        buttonSavePreferences = findViewById(R.id.savePreferences);
        buttonChangeAudioQuality = findViewById(R.id.btQuality);

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
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();
        connection.openDatabase();

        if(avatarManager.isAvatarCreated()) {
            editTextName.setText(avatarDataAccess.getAvatarName());
            editTextAge.setText(String.valueOf(avatarDataAccess.getAvatarAge()));

            String[] preferencesData = preferencesDataAccess.getPreferencesData();
            if (preferencesData[0].equals("1")) checkBoxRecordAudios.setChecked(true);
            if (preferencesData[1].equals("1")) checkBoxSaveRecordings.setChecked(true);
        }

        if (preferencesDataAccess.getAudioQuality()) buttonChangeAudioQuality.setText("ALTA");
        else buttonChangeAudioQuality.setText("BAJA");

        setTheme();
    }

    private void setUserData(String name, byte age) {
        if (isValidUserData(name, age)) {
            savePreferencesData();
            Toast.makeText(this, "PREFERENCIAS GUARDADAS", Toast.LENGTH_SHORT).show();

            if (avatarManager.isAvatarCreated()) {
                avatarDataUpdate.updateNameAndAge(name, age);
                finish();
                goToMainMenu();
            } else {
                preferencesDataAccess.setDefaultAudioQuality();
                goToCharacterChoice(name, age);
            }
        } else {
            Toast.makeText(this, "DATOS INGRESADOS NO VALIDOS", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidUserData(@NonNull String name, byte age) {
        return !name.isEmpty() && age > 0;
    }
    private void savePreferencesData() {
        boolean recordSnoring = checkBoxRecordAudios.isChecked();
        boolean saveAudios = checkBoxSaveRecordings.isChecked();
        preferencesDataAccess.updatePreferences(saveAudios, recordSnoring);
    }

    @SuppressLint("SetTextI18n")
    private void updateQuality() {
        PreferencesDataAccess preferencesDataAccess = new PreferencesDataAccess(connection);
        boolean audioQuality = preferencesDataAccess.getAudioQuality();
        preferencesDataAccess.updateAudioQuality(!audioQuality);

        if (preferencesDataAccess.getAudioQuality()) buttonChangeAudioQuality.setText("ALTA");
        else buttonChangeAudioQuality.setText("BAJA");

        String quality = audioQuality ? "BAJA" : "ALTA";
        Toast toast = Toast.makeText(this, "CALIDAD DE AUDIO ACTUALIZADA A " + quality, Toast.LENGTH_SHORT);
        toast.show();
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

    private void setTheme() {
        View view = findViewById(R.id.configuration);

        Themes.setBackgroundColor(this, view);
        Themes.setButtonTheme(this, buttonSavePreferences);
        Themes.setButtonDataTheme(this, buttonChangeAudioQuality);
    }
}