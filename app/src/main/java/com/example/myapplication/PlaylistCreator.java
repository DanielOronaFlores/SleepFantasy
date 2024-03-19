package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import java.util.List;
import java.util.Objects;

import Adapters.AdapterChecklistSongs;
import Database.DataAccess.SongsDataAccess;
import Database.DatabaseConnection;
import Dialogs.PlaylistCreatorFragment;
import Models.Audio;

public class PlaylistCreator extends AppCompatActivity {
    private final DatabaseConnection connection = DatabaseConnection.getInstance(this);
    private final SongsDataAccess songsDataAccess = new SongsDataAccess(connection);
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_creator);

        connection.openDatabase();
        List<Audio> songs = songsDataAccess.getAllSongs();

        recyclerView = findViewById(R.id.recyclerSongs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new AdapterChecklistSongs(songs));

        Button createPlaylist = findViewById(R.id.createPlaylist);
        createPlaylist.setOnClickListener(v -> openPlaylistSelectionNameDialog());
    }

    private void openPlaylistSelectionNameDialog() {
        List<Audio> selectedSongs;
        selectedSongs = ((AdapterChecklistSongs) Objects.requireNonNull(recyclerView.getAdapter())).getSelectedSongs();

        PlaylistCreatorFragment playlistCreator = new PlaylistCreatorFragment();
        playlistCreator.setSelectedSongs(selectedSongs);
        playlistCreator.show(getSupportFragmentManager(), "PlaylistCreator");
    }
}