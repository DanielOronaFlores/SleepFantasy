package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chibde.visualizer.LineVisualizer;

import java.io.File;

import Database.DatabaseConnection;
import Files.AudiosFiles;

public class Recordings extends AppCompatActivity {
    TextView textView;
    MediaPlayer mediaPlayer = new MediaPlayer();
    AudiosFiles audiosFiles = new AudiosFiles();
    Button elimnarButton;
    DatabaseConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordings);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Se necesita otorgar el permiso de grabar audios para esta sección :)", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            LineVisualizer lineVisualizer = findViewById(R.id.visualizer);
            lineVisualizer.setColor(ContextCompat.getColor(this, R.color.black));
            lineVisualizer.setStrokeWidth(5);
            lineVisualizer.setPlayer(mediaPlayer.getAudioSessionId());
        }

        connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();

        textView = findViewById(R.id.bttn);
        textView.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        });

        elimnarButton = findViewById(R.id.btn_eliminar);
        elimnarButton.setOnClickListener(v -> deleteRecording());
    }

    private void deleteRecording() {
        File file = new File(audiosFiles.getRecordingsPath());
        if (file.exists()) {
            if (file.delete()) Toast.makeText(this, "Audio Eliminado", Toast.LENGTH_SHORT).show();
            else Toast.makeText(this, "No se pudo eliminar el archivo", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No hay audio disponible", Toast.LENGTH_SHORT).show();
        }
    }
}