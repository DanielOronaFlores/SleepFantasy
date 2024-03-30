package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Adapters.AdapterAudios;
import Database.DataAccess.PlaylistDataAccess;
import Database.DataAccess.PlaylistSongsDataAccess;
import Database.DataUpdates.PlaylistDataUpdate;
import Database.DatabaseConnection;
import Models.Audio;

public class PlaylistVisualizer extends AppCompatActivity {
    private final DatabaseConnection connection = DatabaseConnection.getInstance(this);
    private final PlaylistDataAccess playListDataAccess = new PlaylistDataAccess(connection);
    private final PlaylistSongsDataAccess playlistSongsDataAccess = new PlaylistSongsDataAccess(connection);
    private final PlaylistDataUpdate playlistDataUpdate = new PlaylistDataUpdate(connection);
    private RecyclerView recyclerView;
    private AdapterAudios adapterSongs;
    private TextView playlistTitle;
    private ImageView deletePlaylist, editPlaylist;
    private int playlistID;
    private List<Audio> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        connection.openDatabase();

        Intent playlistSelector = getIntent();
        playlistID = playlistSelector.getIntExtra("playlistID", -1);
        if (playlistID == -1) {
            throw new RuntimeException("Playlist ID not found");
        }

        recyclerView = findViewById(R.id.recyclerSongsSelector);
        playlistTitle = findViewById(R.id.playlistTitle);

        deletePlaylist = findViewById(R.id.deletePlaylist);
        deletePlaylist.setOnClickListener(v -> {
            deletePlaylist();
            Toast.makeText(this, "Playlist eliminada", Toast.LENGTH_SHORT).show();
            finish();
        });

        editPlaylist = findViewById(R.id.editPlaylist);
        editPlaylist.setOnClickListener(v -> {
            if (playListDataAccess.isCreatedBySystem(playlistID)) {
                Toast.makeText(this, "No se puede editar una playlist creada por el sistema", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, PlaylistEditor.class);
                intent.putExtra("playlistID", playlistID);
                startActivity(intent);
            }
        });

        if (playListDataAccess.isCreatedBySystem(playlistID)) {
            deletePlaylist.setVisibility(ImageView.GONE);
            editPlaylist.setVisibility(ImageView.GONE);
        }

        setPlaylistTitle();
        setSongsList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        songs = playlistSongsDataAccess.getSongsFromPlaylist(playlistID);
        if (songs.isEmpty()) {
            playlistDataUpdate.deletePlaylist(playlistID);
            finish();
        } else {
            setPlaylistTitle();
            setSongsList();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setPlaylistTitle() {
        String title = playListDataAccess.getPlaylistTitle(playlistID);
        playlistTitle.setText(title);
    }

    private void setSongsList() {
        adapterSongs = new AdapterAudios(songs, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapterSongs);
    }

    private void deletePlaylist() {
        playlistDataUpdate.deletePlaylist(playlistID);
    }
}