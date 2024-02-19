package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chibde.visualizer.LineVisualizer;

import java.io.File;

import Database.DatabaseConnection;
import Files.AudiosFiles;

public class recordings extends AppCompatActivity {
    TextView textView;
    MediaPlayer mediaPlayer = new MediaPlayer();
    AudiosFiles audiosFiles = new AudiosFiles();
    Button calidadButton, elimnarButton;
    DatabaseConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordings);

        connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();

        LineVisualizer lineVisualizer = findViewById(R.id.visualizer);
        //mediaPlayer.setDataSource(filePath);
        //mediaPlayer.prepare();


        //mediaPlayer = MediaPlayer.create(this, R.raw.c);

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

        elimnarButton = findViewById(R.id.btn_eliminar);
        elimnarButton.setOnClickListener(v -> deleteRecording());

        test();
    }

    private void test(){

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

    private int getSamplesPerSegment(float sampleRate, double segmentDuration) {
        return (int) (sampleRate * segmentDuration);
    }
}