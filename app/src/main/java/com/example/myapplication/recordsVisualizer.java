package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.chibde.visualizer.LineVisualizer;

import DataAccess.PreferencesDataAccess;
import Database.DatabaseConnection;


public class recordsVisualizer extends AppCompatActivity {
    TextView textView;
    MediaPlayer mediaPlayer = new MediaPlayer();
    Button calidadButton;
    DatabaseConnection connection;
    String filePath = "/storage/emulated/0/Android/data/com.example.myapplication/cache/ejemplo.wav";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_visualizer);

        connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();

        LineVisualizer lineVisualizer = findViewById(R.id.visualizer);
        //mediaPlayer.setDataSource(filePath);
        //mediaPlayer.prepare();
        mediaPlayer = MediaPlayer.create(this, R.raw.c);

        lineVisualizer.setColor(ContextCompat.getColor(this, R.color.black));
        lineVisualizer.setStrokeWidth(1);
        lineVisualizer.setPlayer(mediaPlayer.getAudioSessionId());

        textView = findViewById(R.id.bttn);
        textView.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        });

        calidadButton = findViewById(R.id.btn_calidad);
        calidadButton.setOnClickListener(v -> updateQuality());
    }

    private void updateQuality() {
        PreferencesDataAccess preferencesDataAccess = new PreferencesDataAccess(connection);
        boolean audioQuality = preferencesDataAccess.getAudioQuality();
        preferencesDataAccess.updateAudioQuality(!audioQuality);
    }
}