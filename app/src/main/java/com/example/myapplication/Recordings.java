package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.chibde.visualizer.LineVisualizer;

import java.io.File;
import java.util.List;

import Adapters.AdapterLoudSounds;
import Recordings.*;
import Recordings.AudioFilter.AudioFilter;
import Recordings.ListSaver.Deserializer;
import Recordings.ListSaver.Sound;
import Recordings.Recorders.PCMRecorder;
import Database.DatabaseConnection;
import Files.AudiosFiles;
import Recordings.Recorders.Recorder;

public class Recordings extends AppCompatActivity {
    private final AudiosFiles audiosFiles = new AudiosFiles();
    private final SecondsCounter secondsCounter = new SecondsCounter();
    private Space space;
    private AdapterLoudSounds adapterLoudSounds;
    private RecyclerView recyclerView;

    PCMRecorder pcmRecorder = new PCMRecorder();
    Recorder recorder = new Recorder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordings);

        //testFiltros();

        adapterLoudSounds = new AdapterLoudSounds(getSoundsList());
        recyclerView = findViewById(R.id.loudSounds_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        space = findViewById(R.id.space);
        if (getSoundsList().isEmpty()) {
            recyclerView.setVisibility(View.GONE);
        } else {
            space.setVisibility(View.GONE);
        }

        MediaPlayer mediaPlayer = adapterLoudSounds.getMediaPlayer();


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
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView.setAdapter(adapterLoudSounds);
        //testFiltros();
    }

    private void deleteRecording() {
        File file = new File(audiosFiles.getRecordingsPath());
        if (file.exists()) {
            if (file.delete()) {
                file = new File(audiosFiles.getXMLPath());
                file.delete();
                Toast.makeText(this, "Audio Eliminado", Toast.LENGTH_SHORT).show();
                recreate();
            }
            else Toast.makeText(this, "No se pudo eliminar el archivo", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No hay audio disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private List<List<Integer>> getSoundsList() {
        Deserializer deserializer = new Deserializer();
        List<Sound> soundsList = deserializer.deserializeFromXML(audiosFiles.getXMLPath());

        Log.d("SOUNDS", soundsList.toString());

        return secondsCounter.getConsecutiveSeconds(soundsList);
    }


    private void testGrabacion() {
        pcmRecorder.startRecording();
        recorder.startRecording();
    }
    private void testFiltros() {
        AudioFilter audioFilter = new AudioFilter();
        audioFilter.filterAudio();
    }
}