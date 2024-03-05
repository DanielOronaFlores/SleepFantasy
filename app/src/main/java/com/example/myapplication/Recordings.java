package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chibde.visualizer.LineVisualizer;

import java.io.File;
import java.util.List;

import Recordings.*;
import Recordings.AudioFilter.AudioFilter;
import Recordings.ListSaver.Deserializer;
import Recordings.ListSaver.Sound;
import Recordings.Recorders.PCMRecorder;
import Database.DatabaseConnection;
import Files.AudiosFiles;

public class Recordings extends AppCompatActivity {
    private final MediaPlayer mediaPlayer = new MediaPlayer();
    private final AudiosFiles audiosFiles = new AudiosFiles();
    private final SecondsCounter secondsCounter = new SecondsCounter();

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

        DatabaseConnection connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();

        TextView textView = findViewById(R.id.bttn);
        textView.setOnClickListener(v -> {
            testGrabacion();
        });

        Button elimnarButton = findViewById(R.id.btn_eliminar);
        elimnarButton.setOnClickListener(v -> deleteRecording());

        testFiltros();
        getSoundsList();
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

    private void getSoundsList() {
        List<Sound> soundsList = Deserializer.loadFromFile(this, "sounds.xml");
        List<List<Integer>> consecutiveSeconds = secondsCounter.getConsecutiveSeconds(soundsList);
        //Imprimir en el log
        for (List<Integer> list : consecutiveSeconds) {
            Log.d("ConsecutiveSeconds", list.toString());
        }
    }


    private void testGrabacion() {
        PCMRecorder audioRecorder = new PCMRecorder();
        audioRecorder.startRecording();
    }
    private void testFiltros() {
        AudioFilter audioFilter = new AudioFilter();
        audioFilter.filterAudio();
    }
}