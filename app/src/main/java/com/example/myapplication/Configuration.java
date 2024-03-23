package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Database.DatabaseConnection;
import Database.DataAccess.PreferencesDataAccess;

public class Configuration extends AppCompatActivity {
    private DatabaseConnection connection;
    private EditText etName, etAge;
    private CheckBox ckSaveAudios, ckRecordSnoring;
    private DatabaseConnection.AvatarCreator avatarManager;
    private Database.DataUpdates.AvatarDataUpdate avatarDataUpdate;
    private PreferencesDataAccess preferencesManager;
    private Button btQuality;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();

        etName = findViewById(R.id.name);
        etAge = findViewById(R.id.age);
        ckRecordSnoring = findViewById(R.id.recordSnoring);
        ckSaveAudios = findViewById(R.id.saveAudios);
        Button btSave = findViewById(R.id.savePreferences);
        btQuality = findViewById(R.id.btQuality);

        PreferencesDataAccess preferencesDataAccess = new PreferencesDataAccess(connection);
        if (preferencesDataAccess.getAudioQuality()) btQuality.setText("alta");
        else btQuality.setText("baja");

        Database.DataAccess.AvatarDataAccess avatarDataAccess = new Database.DataAccess.AvatarDataAccess(connection);
        avatarManager = new DatabaseConnection.AvatarCreator(connection);
        avatarDataUpdate = new Database.DataUpdates.AvatarDataUpdate(connection);
        preferencesManager = new PreferencesDataAccess(connection);

        if(avatarManager.isAvatarCreated()) {
            etName.setText(avatarDataAccess.getAvatarName());
            etAge.setText(String.valueOf(avatarDataAccess.getAvatarAge()));

            String[] preferencesData = preferencesManager.getPreferencesData();
            if (preferencesData[0].equals("1")) ckRecordSnoring.setChecked(true);
            if (preferencesData[1].equals("1")) ckSaveAudios.setChecked(true);
        }
        btSave.setOnClickListener(view ->
                setUserData(etName.getText().toString(), Byte.parseByte(etAge.getText().toString()))
        );
        btQuality.setOnClickListener(view -> updateQuality());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.closeDatabase();
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
                preferencesManager.setDefaultAudioQuality();
                goToCharacterChoice(name, age);
            }
        } else {
            Toast.makeText(this, "DATOS INGRESADOS NO VALIDOS", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isValidUserData(String name, byte age) {
        return !name.isEmpty() && age > 0;
    }
    public void savePreferencesData() {
        boolean recordSnoring = ckRecordSnoring.isChecked();
        boolean saveAudios = ckSaveAudios.isChecked();
        preferencesManager.updatePreferences(recordSnoring, saveAudios);
    }
    private void updateQuality() {
        PreferencesDataAccess preferencesDataAccess = new PreferencesDataAccess(connection);
        boolean audioQuality = preferencesDataAccess.getAudioQuality();
        preferencesDataAccess.updateAudioQuality(!audioQuality);

        if (preferencesDataAccess.getAudioQuality()) btQuality.setText("alta");
        else btQuality.setText("baja");

        String quality = audioQuality ? "baja" : "alta";
        Toast toast = Toast.makeText(this, "Calidad de audio actualizada a " + quality, Toast.LENGTH_SHORT);
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
}
