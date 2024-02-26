package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import Adapters.AdapterPlaylistCreator;
import DataAccess.PlaylistDataAccess;
import DataAccess.PlaylistSongsDataAccess;
import DataUpdates.PlaylistDataUpdate;
import DataUpdates.PlaylistSongsDataUpdate;
import Database.DatabaseConnection;
import Music.Song;

public class PlaylistEditor extends AppCompatActivity {
    private final DatabaseConnection connection = DatabaseConnection.getInstance(this);
    private final PlaylistSongsDataAccess playlistSongsDataAccess = new PlaylistSongsDataAccess(connection);
    private final PlaylistDataAccess playlistDataAccess = new PlaylistDataAccess(connection);
    private final PlaylistDataUpdate playlistDataUpdate = new PlaylistDataUpdate(connection);
    private final PlaylistSongsDataUpdate playlistSongsDataUpdate = new PlaylistSongsDataUpdate(connection);
    private RecyclerView recyclerView;
    private EditText playlistName;
    private List<Song> songs, selectedSongs;
    private boolean addSongsMode;
    private int playlistID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_editor);

        connection.openDatabase();

        Intent intent = getIntent();
        playlistID = intent.getIntExtra("playlistID", -1);

        playlistName = findViewById(R.id.editPlaylistName);
        playlistName.setText(playlistDataAccess.getPlaylistTitle(playlistID));

        Button addSongs = findViewById(R.id.addSongs);
        addSongs.setOnClickListener(v -> showNotInPlaylist(playlistID));

        Button deleteSongs = findViewById(R.id.deleteSongs);
        deleteSongs.setOnClickListener(v -> showInPlaylist(playlistID));

        Button editPlaylist = findViewById(R.id.editPlaylist);
        editPlaylist.setOnClickListener(v -> updatePlaylist());

        recyclerView = findViewById(R.id.recyclerEditSongs);
    }

    private void generateSongsList(List<Song> songs) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new AdapterPlaylistCreator(songs));
    }

    private void showInPlaylist(int playlistID) {
        addSongsMode = false;
        songs = playlistSongsDataAccess.getSongsFromPlaylist(playlistID);
        generateSongsList(songs);
    }
    private void showNotInPlaylist(int playlistID) {
        addSongsMode = true;
        songs = playlistSongsDataAccess.getNotSongsFromPlaylist(playlistID);
        generateSongsList(songs);
    }

    private void deleteSongsFromPlaylist() {
        for (Song song : selectedSongs) {
            playlistSongsDataUpdate.deleteSongFromPlaylist(playlistID, song.getId());
        }
    }
    private void addSongsToPlaylist() {
        for (Song song : selectedSongs) {
            playlistSongsDataUpdate.addSongToPlaylist(playlistID, song.getId());
        }
    }

    private boolean isEditingSongs() {
        return recyclerView.getAdapter() instanceof AdapterPlaylistCreator;
    }
    private void updatePlaylistName(String name) {
        playlistDataUpdate.updatePlaylist(playlistID, name);
        Toast.makeText(this, "Nombre playlist actualizada", Toast.LENGTH_SHORT).show();
    }

    private void updatePlaylist() {
        String name = playlistName.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Nombre de playlist no puede estar vacío", Toast.LENGTH_SHORT).show();
        } else {
            if (isEditingSongs()) {
                selectedSongs = ((AdapterPlaylistCreator) Objects.requireNonNull(recyclerView.getAdapter())).getSelectedSongs();
                if (addSongsMode) {
                    addSongsToPlaylist();
                    Toast.makeText(this, "Audios agregados a la playlist", Toast.LENGTH_SHORT).show();
                } else {
                    deleteSongsFromPlaylist();
                    Toast.makeText(this, "Audios eliminados de la playlist", Toast.LENGTH_SHORT).show();
                    if (selectedSongs.size() == songs.size()) {
                        Toast.makeText(this, "No hay audios en la playlist, lista eliminada", Toast.LENGTH_SHORT).show();
                        playlistDataUpdate.deletePlaylist(playlistID);
                    }
                }
            } else {
                updatePlaylistName(name);
            }
            finish();
        }
    }
}