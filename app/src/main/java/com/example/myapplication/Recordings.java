package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.chibde.visualizer.LineVisualizer;

import java.io.File;
import java.util.List;

import Adapters.AdapterLoudSounds;
import AudioFilter.AudioFilter;
import Serializers.Deserializer;
import Models.Sound;
import Recorders.PCMRecorder;
import Database.DatabaseConnection;
import Files.AudiosPaths;
import Recorders.Recorder;
import Utils.SecondsCounter;

public class Recordings extends AppCompatActivity {
    private final AudiosPaths audiosFiles = new AudiosPaths();
    private final SecondsCounter secondsCounter = new SecondsCounter();
    private AdapterLoudSounds adapterLoudSounds;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordings);

        adapterLoudSounds = new AdapterLoudSounds(getSoundsList());
        recyclerView = findViewById(R.id.loudSounds_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Space space = findViewById(R.id.space);
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
            lineVisualizer.setColor(ContextCompat.getColor(this, R.color.white));
            lineVisualizer.setStrokeWidth(5);
            lineVisualizer.setPlayer(mediaPlayer.getAudioSessionId());
        }

        DatabaseConnection connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();


        Button elimnarButton = findViewById(R.id.btn_eliminar);
        elimnarButton.setOnClickListener(v -> deleteRecording());
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView.setAdapter(adapterLoudSounds);
    }

    private void deleteRecording() {
        if (areFilesExist()) {
            if (areFilesDeleted()) {
                Toast.makeText(this, "Audio Eliminado", Toast.LENGTH_SHORT).show();
                recreate();
            } else Toast.makeText(this, "No se pudo eliminar el archivo", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "No hay audio disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private List<List<Integer>> getSoundsList() {
        Deserializer deserializer = new Deserializer();
        List<Sound> soundsList = deserializer.deserializeFromXML(audiosFiles.getXMLPath());

        return secondsCounter.getConsecutiveSeconds(soundsList);
    }

    private boolean areFilesDeleted() {
        File file3GP = new File(audiosFiles.getRecordingsPath());
        File filePCM = new File(audiosFiles.getPCMPath());
        File fileXML = new File(audiosFiles.getXMLPath());
        return file3GP.delete() && filePCM.delete() && fileXML.delete();
    }

    private boolean areFilesExist() {
        File file3GP = new File(audiosFiles.getRecordingsPath());
        File filePCM = new File(audiosFiles.getPCMPath());
        File fileXML = new File(audiosFiles.getXMLPath());
        return file3GP.exists() && filePCM.exists() && fileXML.exists();
    }
}