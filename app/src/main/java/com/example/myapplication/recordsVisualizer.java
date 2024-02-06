package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chibde.visualizer.LineVisualizer;

import Audio.AudioRecorder;
import DataAccess.PreferencesDataAccess;
import Database.DatabaseConnection;
import Files.AudiosFiles;

public class recordsVisualizer extends AppCompatActivity {
    TextView textView;
    MediaPlayer mediaPlayer = new MediaPlayer();
    AudiosFiles audiosFiles = new AudiosFiles();
    Button calidadButton, elimnarButton;
    DatabaseConnection connection;
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
        lineVisualizer.setStrokeWidth(5);
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

        elimnarButton = findViewById(R.id.btn_eliminar);
        elimnarButton.setOnClickListener(v -> placeHolderFuncion());
    }

    private void updateQuality() {
        PreferencesDataAccess preferencesDataAccess = new PreferencesDataAccess(connection);
        boolean audioQuality = preferencesDataAccess.getAudioQuality();
        preferencesDataAccess.updateAudioQuality(!audioQuality);

        String quality = audioQuality ? "baja" : "alta";
        Toast toast = Toast.makeText(this, "Calidad de audio actualizada a " + quality, Toast.LENGTH_SHORT);
        toast.show();
    }
    private void placeHolderFuncion() {
        //TODO: Implementar método de eliminación de grabación
        Log.d("PLACEHOLDER", "Función de eliminación de grabación");


    }
}