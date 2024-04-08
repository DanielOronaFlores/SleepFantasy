package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

import Adapters.AdapterPlaylists;
import Database.DataAccess.PlaylistDataAccess;
import Database.DatabaseConnection;
import Models.Playlist;
import Styles.Themes;

public class PlaylistSelector extends AppCompatActivity {
    private final DatabaseConnection connection = DatabaseConnection.getInstance(this);
    private PlaylistDataAccess playlistDataAccess;
    private List<Playlist> playlists;
    private RecyclerView recyclerView;
    private ImageView timer;
    private Button addAudios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_selector);

        recyclerView = findViewById(R.id.recyclerPlaylists);

        Button createPlaylist = findViewById(R.id.createNewPlaylist);
        createPlaylist.setOnClickListener(v -> goToCreatePlaylist());

        addAudios = findViewById(R.id.addAudios);
        addAudios.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddAudios.class);
            startActivity(intent);
        });

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
        recyclerView.setAdapter(new AdapterPlaylists(playlists));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setTheme();
    }

    private void setTheme() {
        Themes.setBackgroundColor(this, findViewById(R.id.playlistSelector));

        Themes.setButtonTheme(this, findViewById(R.id.createNewPlaylist));
        Themes.setButtonTheme(this, findViewById(R.id.addAudios));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void goToCreatePlaylist() {
        Intent intent = new Intent(this, PlaylistCreator.class);
        startActivity(intent);
    }
}