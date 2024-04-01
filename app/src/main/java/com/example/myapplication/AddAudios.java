package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;
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
import Database.DataAccess.SongsDataAccess;
import Database.DataUpdates.PlaylistDataUpdate;
import Database.DataUpdates.PlaylistSongsDataUpdate;
import Database.DataUpdates.SongsDataUpdate;
import Database.DatabaseConnection;
import GameManagers.Challenges.ChallengesUpdater;

public class AddAudios extends AppCompatActivity {
    private DatabaseConnection connection;
    private SongsDataUpdate songsDataUpdate;
    private SongsDataAccess songsDataAccess;
    private PlaylistDataUpdate playlistDataUpdate;
    private PlaylistDataAccess playlistDataAccess;
    private PlaylistSongsDataUpdate playlistSongsDataUpdate;
    private RecyclerView recyclerView;
    private Button addAudios;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_audios);

        connection = DatabaseConnection.getInstance(this);
        songsDataUpdate = new SongsDataUpdate(connection);
        songsDataAccess = new SongsDataAccess(connection);
        playlistDataUpdate = new PlaylistDataUpdate(connection);
        playlistDataAccess = new PlaylistDataAccess(connection);
        playlistSongsDataUpdate = new PlaylistSongsDataUpdate(connection);

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
    }

    private void addSelectedAudios() {
        List<String> selectedAudios = ((AdapterAddAudios) Objects.requireNonNull(recyclerView.getAdapter())).getSelectedAudios();
        if (!selectedAudios.isEmpty()) {
            if (!playlistDataAccess.isPlaylistCreated("Audio Propios")) {
                playlistDataUpdate.createPlaylist("Audio Propios", true);
            }

            int playListID = playlistDataAccess.getPlaylistId("Audio Propios");

            for (String audio : selectedAudios) {
                songsDataUpdate.addSong(audio);
                int songID = songsDataAccess.getSongID(audio);
                playlistSongsDataUpdate.addSongToPlaylist(playListID, songID);
            }
            Toast.makeText(this, "AUDIOS AGREGADOS CORRECTAMENTE", Toast.LENGTH_SHORT).show();
            ChallengesUpdater challengesUpdater = new ChallengesUpdater(connection);
            challengesUpdater.updateAddAudio();
            finish();
        }
    }
}