package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import AudioFilter.AudioFilter;
import Database.DataAccess.AvatarDataAccess;
import Database.DataAccess.PreferencesDataAccess;
import Database.DataUpdates.AvatarCreator;
import Database.DataUpdates.AvatarDataUpdate;
import Database.DataUpdates.PreferencesDataUpdate;
import Database.DatabaseConnection;
import Files.AudiosPaths;
import GameManagers.Challenges.ChallengesManager;
import Permissions.Permissions;
import SleepEvaluator.SleepEvaluator;
import SleepEvaluator.Trainer.BayesCreator;
import Styles.Themes;
import Tips.Tips;

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
                setUserData(editTextName.getText().toString(), editTextAge.getText().toString())
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

        Tips tips = new Tips();
        tips.updateTip();

        ChallengesManager challengesManager = new ChallengesManager();
        challengesManager.manageChallenges();

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(AudiosPaths.getRecordings3GPPath());
        float sampleRate = Float.parseFloat(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_SAMPLERATE));

        AudioFilter.startFilter(sampleRate);

        //BayesCreator.createProbabilities();
        //case1();
        //case2();
        //case3();
        //case4();
        //case5();
        //case6();
        //case7();
        //case8();
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

    private void setUserData(String name, String age) {
        if (name.isEmpty()) {
            Toast.makeText(this, "INGRESE UN NOMBRE", Toast.LENGTH_SHORT).show();
        } else if (age.isEmpty()) {
            Toast.makeText(this, "INGRESE UNA EDAD", Toast.LENGTH_SHORT).show();
        } else {
            byte ageByte = Byte.parseByte(age);
            if (ageByte <= 0 || ageByte >= 100) {
                Toast.makeText(this, "EDAD INVALIDA", Toast.LENGTH_SHORT).show();
                return;
            }

            preferencesDataUpdate.updatePreferences(checkBoxSaveRecordings.isChecked(), checkBoxRecordAudios.isChecked());
            if (avatarManager.isAvatarCreated()) {
                avatarDataUpdate.updateNameAndAge(name, ageByte);
                finish();
                goToMainMenu();
            } else {
                goToCharacterChoice(name, ageByte);
            }
            Toast.makeText(this, "PREFERENCIAS GUARDADAS", Toast.LENGTH_SHORT).show();
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


    // ------------- CASOS
    private void case1() {
        boolean[] monsters = {true, false, false, true, false};

        SleepEvaluator sleepEvaluator = new SleepEvaluator();
        sleepEvaluator.evaluate(
                10,
                240,
                120,
                120,
                0,
                4,
                0,
                monsters
        );
    }

    private void case2() {
        boolean[] monsters = {false, false, false, false, false};

        SleepEvaluator sleepEvaluator = new SleepEvaluator();
        sleepEvaluator.evaluate(110,
                215,
                130,
                100,
                2,
                1,
                2,
                monsters
        );
    }

    private void case3() {
        boolean[] monsters = {false, false, false, false, false};

        SleepEvaluator sleepEvaluator = new SleepEvaluator();
        sleepEvaluator.evaluate(60,
                215,
                130,
                130,
                2,
                1,
                4,
                monsters
        );
    }

    private void case4() {
        boolean[] monsters = {false, false, false, false, false};

        SleepEvaluator sleepEvaluator = new SleepEvaluator();
        sleepEvaluator.evaluate(60,
                175,
                50,
                25,
                8,
                20,
                10,
                monsters
        );
    }

    private void case5() {
        boolean[] monsters = {false, false, false, false, false};

        SleepEvaluator sleepEvaluator = new SleepEvaluator();
        sleepEvaluator.evaluate(90,
                60,
                20,
                10,
                5,
                9,
                15,
                monsters
        );
    }

    private void case6() {
        boolean[] monsters = {false, false, false, false, false};

        SleepEvaluator sleepEvaluator = new SleepEvaluator();
        sleepEvaluator.evaluate(120,
                60,
                20,
                10,
                5,
                9,
                10,
                monsters
        );
    }

    private void case7() {
        boolean[] monsters = {false, false, false, false, false};

        SleepEvaluator sleepEvaluator = new SleepEvaluator();
        sleepEvaluator.evaluate(90,
                60,
                20,
                10,
                5,
                9,
                22,
                monsters
        );
    }

    private void case8() {
        boolean[] monsters = {false, false, false, false, false};

        SleepEvaluator sleepEvaluator = new SleepEvaluator();
        sleepEvaluator.evaluate(0,
                300,
                100,
                50,
                3,
                10,
                7,
                monsters
        );
    }
}