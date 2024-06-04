package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import java.util.List;
import java.util.Objects;

import Adapters.AdapterChecklistAudios;
import Database.DataAccess.AudiosDataAccess;
import Database.DatabaseConnection;
import Dialogs.PlaylistCreatorFragment;
import Models.Audio;
import Styles.Themes;

public class PlaylistCreator extends AppCompatActivity {
    private DatabaseConnection connection;
    private AudiosDataAccess audiosDataAccess;
    private RecyclerView recyclerView;
    private Button createPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_creator);

        connection = DatabaseConnection.getInstance(this);
        audiosDataAccess = new AudiosDataAccess(connection);

        List<Audio> Audios = audiosDataAccess.getAllAudios();

        recyclerView = findViewById(R.id.recyclerAudios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new AdapterChecklistAudios(Audios));

        createPlaylist = findViewById(R.id.createPlaylist);
        createPlaylist.setOnClickListener(v -> openPlaylistSelectionNameDialog());
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTheme();
    }

    private void setTheme() {
        Themes.setBackgroundColor(this, findViewById(R.id.playlistCreator));
        Themes.setButtonTheme(this, createPlaylist);
    }

    private void openPlaylistSelectionNameDialog() {
        List<Audio> selectedAudios = ((AdapterChecklistAudios) Objects.requireNonNull(recyclerView.getAdapter())).getSelectedAudios();

        PlaylistCreatorFragment playlistCreator = new PlaylistCreatorFragment();
        playlistCreator.setSelectedAudios(selectedAudios);
        playlistCreator.show(getSupportFragmentManager(), "PlaylistCreator");
    }
}