package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import Adapters.AdapterSongs;
import DataAccess.SongsDataAccess;
import Database.DatabaseConnection;
import Dialogs.PlaylistCreator;
import Songs.Song;

public class playlistCreator extends AppCompatActivity {
    private DatabaseConnection connection;
    private SongsDataAccess songsDataAccess;
    private List<Song> songs;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_creator);

        connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();
        songsDataAccess = new SongsDataAccess(connection);
        songs = songsDataAccess.getAllSongs();

        recyclerView = findViewById(R.id.recyclerSongs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new AdapterSongs(songs));

        Button createPlaylist = findViewById(R.id.createPlaylist);
        createPlaylist.setOnClickListener(v -> goToPlayListSelectionName());
    }

    private void goToPlayListSelectionName() {
        List<Song> selectedSongs = new ArrayList<>();
        selectedSongs = ((AdapterSongs) recyclerView.getAdapter()).getSelectedSongs();

        PlaylistCreator playlistCreator = new PlaylistCreator();
        playlistCreator.setSelectedSongs(selectedSongs);
        playlistCreator.show(getSupportFragmentManager(), "PlaylistCreator");
    }
}