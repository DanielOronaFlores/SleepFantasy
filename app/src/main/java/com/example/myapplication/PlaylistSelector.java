package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

import Adapters.AdapterPlaylists;
import Database.DataAccess.PlaylistDataAccess;
import Database.DatabaseConnection;
import Music.Playlist;

public class PlaylistSelector extends AppCompatActivity {
    private final DatabaseConnection connection = DatabaseConnection.getInstance(this);
    private PlaylistDataAccess playlistDataAccess;
    private List<Playlist> playlists;
    private RecyclerView recyclerView;
    private ImageView timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_selector);

        recyclerView = findViewById(R.id.recyclerPlaylists);

        Button createPlaylist = findViewById(R.id.createNewPlaylist);
        createPlaylist.setOnClickListener(v -> goToCreatePlaylist());

        timer = findViewById(R.id.buttonTemporizer);
        timer.setOnClickListener(v -> {
            Intent intent = new Intent(this, Timer.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        connection.openDatabase();
        playlistDataAccess = new PlaylistDataAccess(connection);
        playlists = playlistDataAccess.getAllPlaylists();
        Log.d("playlist", "Playlists: " + playlists.size());
        recyclerView.setAdapter(new AdapterPlaylists(playlists));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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