package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Database.Avatar.AvatarDataAccess;
import Database.Avatar.AvatarDataUpdate;
import Database.Avatar.AvatarManager;
import Database.DatabaseConnection;
import Database.Preferences.PreferencesDataAccess;

public class configuration extends AppCompatActivity {
    private EditText etName, etAge;
    private CheckBox ckSaveAudios, ckRecordSnoring;
    private DatabaseConnection connection;
    private AvatarManager avatarManager;
    private AvatarDataUpdate avatarDataUpdate;
    private PreferencesDataAccess preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        etName = findViewById(R.id.name);
        etAge = findViewById(R.id.age);
        ckRecordSnoring = findViewById(R.id.recordSnoring);
        ckSaveAudios = findViewById(R.id.saveAudios);
        Button btSave = findViewById(R.id.savePreferences);

        connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();

        AvatarDataAccess avatarDataAccess = new AvatarDataAccess(connection);
        avatarManager = new AvatarManager(connection);
        avatarDataUpdate = new AvatarDataUpdate(connection);
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
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.closeDatabase();
    }

    private void setUserData(String name, byte age) {
        if (isValidUserData(name, age)) {
            savePreferencesData();
            Toast.makeText(this, "PREFERENCIAS GUARDADAS", Toast.LENGTH_LONG).show();

            if (avatarManager.isAvatarCreated()) {
                avatarDataUpdate.updateNameAndAge(name, age);
                goToMainMenu();
            } else {
                goToCharacterChoice(name, age);
            }
        } else {
            Toast.makeText(this, "DATOS INGRESADOS NO VALIDOS", Toast.LENGTH_LONG).show();
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

    private void goToMainMenu() {
        Intent intent = new Intent(this, mainMenu.class);
        startActivity(intent);
    }
    private void goToCharacterChoice(String name, byte age) {
        Intent intent = new Intent(this, characterChoice.class);
        intent.putExtra("name", name);
        intent.putExtra("age", age);
        startActivity(intent);
    }
}