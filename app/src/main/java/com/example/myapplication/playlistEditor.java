package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import Adapters.AdapterEditSongs;
import Adapters.AdapterPlaylistCreator;
import Adapters.AdapterSongs;
import DataAccess.PlaylistDataAccess;
import DataAccess.PlaylistSongsDataAccess;
import DataAccess.SongsDataAccess;
import DataUpdates.PlaylistDataUpdate;
import DataUpdates.PlaylistSongsDataUpdate;
import Database.DatabaseConnection;
import Music.Song;

public class playlistEditor extends AppCompatActivity {
    private final DatabaseConnection connection = DatabaseConnection.getInstance(this);
    private PlaylistSongsDataAccess playlistSongsDataAccess = new PlaylistSongsDataAccess(connection);
    private PlaylistDataAccess playlistDataAccess = new PlaylistDataAccess(connection);
    private PlaylistDataUpdate playlistDataUpdate = new PlaylistDataUpdate(connection);
    private PlaylistSongsDataUpdate playlistSongsDataUpdate = new PlaylistSongsDataUpdate(connection);
    private List<Song> songs;
    private RecyclerView recyclerView;
    private EditText playlistName;
    private boolean isAddSongs;
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
        recyclerView.setAdapter(new AdapterEditSongs(songs));
    }

    private void showInPlaylist(int playlistID) {
        isAddSongs = false;
        songs = playlistSongsDataAccess.getSongsFromPlaylist(playlistID);
        generateSongsList(songs);
    }
    private void showNotInPlaylist(int playlistID) {
        isAddSongs = true;
        songs = playlistSongsDataAccess.getNotSongsFromPlaylist(playlistID);
        generateSongsList(songs);
    }

    private void updatePlaylist() {
        String name = playlistName.getText().toString();
        playlistDataUpdate.updatePlaylist(playlistID, name);

        if (name.isEmpty()) {
            Toast.makeText(this, "Nombre de playlist no puede estar vacío", Toast.LENGTH_SHORT).show();
        } else {
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter instanceof AdapterEditSongs) {
                List<Song> selectedSongs = ((AdapterEditSongs) adapter).getSelectedSongs();
                if (isAddSongs) {
                    for (Song song : selectedSongs) {
                        playlistSongsDataUpdate.addSongToPlaylist(playlistID, song.getId());
                    }
                    Toast.makeText(this, "Audios agregados a la playlist", Toast.LENGTH_SHORT).show();
                } else {
                    for (Song song : selectedSongs) {
                        playlistSongsDataUpdate.deleteSongFromPlaylist(playlistID, song.getId());
                    }
                    Toast.makeText(this, "Audios eliminados de la playlist", Toast.LENGTH_SHORT).show();
                }
                finish();
            } else {
                playlistDataUpdate.updatePlaylist(playlistID, name);
                Toast.makeText(this, "Nombre playlist actualizada", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

}