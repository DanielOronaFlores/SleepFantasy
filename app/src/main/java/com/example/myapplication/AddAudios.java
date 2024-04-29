package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import Adapters.AdapterAddAudios;
import Database.DataAccess.PlaylistDataAccess;
import Database.DataAccess.AudiosDataAccess;
import Database.DataUpdates.PlaylistDataUpdate;
import Database.DataUpdates.PlaylistAudiosDataUpdate;
import Database.DataUpdates.AudiosDataUpdate;
import Database.DatabaseConnection;
import GameManagers.Challenges.ChallengesUpdater;
import Styles.Themes;

public class AddAudios extends AppCompatActivity {
    private DatabaseConnection connection;
    private AudiosDataUpdate AudiosDataUpdate;
    private AudiosDataAccess AudiosDataAccess;
    private PlaylistDataUpdate playlistDataUpdate;
    private PlaylistDataAccess playlistDataAccess;
    private PlaylistAudiosDataUpdate PlaylistAudiosDataUpdate;
    private RecyclerView recyclerView;
    private Button addAudios;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_audios);

        connection = DatabaseConnection.getInstance(this);
        AudiosDataUpdate = new AudiosDataUpdate(connection);
        AudiosDataAccess = new AudiosDataAccess(connection);
        playlistDataUpdate = new PlaylistDataUpdate(connection);
        playlistDataAccess = new PlaylistDataAccess(connection);
        PlaylistAudiosDataUpdate = new PlaylistAudiosDataUpdate(connection);

        recyclerView = findViewById(R.id.newAudiosRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new AdapterAddAudios());

        addAudios = findViewById(R.id.addSelectedAudios);
        addAudios.setOnClickListener(v -> addSelectedAudios());
    }

    @Override
    protected void onStart() {
        super.onStart();
        connection.openDatabase();
        setTheme();
    }

    private void addSelectedAudios() {
        List<String> selectedAudios = ((AdapterAddAudios) Objects.requireNonNull(recyclerView.getAdapter())).getSelectedAudios();
        if (!selectedAudios.isEmpty()) {
            if (!playlistDataAccess.isPlaylistCreated("Audio Propios")) {
                playlistDataUpdate.createPlaylist("Audio Propios", true);
            }

            int playListID = playlistDataAccess.getPlaylistId("Audio Propios");

            for (String audio : selectedAudios) {
                AudiosDataUpdate.addAudio(audio, false);
                int audioID = AudiosDataAccess.getaudioID(audio);
                PlaylistAudiosDataUpdate.addaudioToPlaylist(playListID, audioID);
            }
            Toast.makeText(this, "AUDIOS AGREGADOS CORRECTAMENTE", Toast.LENGTH_SHORT).show();
            ChallengesUpdater challengesUpdater = new ChallengesUpdater(connection);
            challengesUpdater.updateAddAudio();
            finish();
        }
    }

    private void setTheme() {
        View view = findViewById(R.id.addAudios);

        Themes.setBackgroundColor(this, view);
        Themes.setButtonTheme(this, addAudios);
    }
}