package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.util.List;

import Adapters.AdapterPlaylists;
import DataAccess.PlaylistDataAccess;
import Database.DatabaseConnection;
import Music.Playlist;

public class PlaylistSelector extends AppCompatActivity {
    private final DatabaseConnection connection = DatabaseConnection.getInstance(this);
    private final PlaylistDataAccess playlistDataAccess = new PlaylistDataAccess(connection);
    private List<Playlist> playlists;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_selector);

        connection.openDatabase();
        playlists = playlistDataAccess.getAllPlaylists();

        Button createPlaylist = findViewById(R.id.createNewPlaylist);
        createPlaylist.setOnClickListener(v -> goToCreatePlaylist());

        recyclerView = findViewById(R.id.recyclerPlaylists);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new AdapterPlaylists(playlists));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        playlists = playlistDataAccess.getAllPlaylists();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new AdapterPlaylists(playlists));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.closeDatabase();
    }

    private void goToCreatePlaylist() {
        Intent intent = new Intent(this, PlaylistCreator.class);
        startActivity(intent);
    }
}