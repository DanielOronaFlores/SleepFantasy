package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Adapters.AdapterSongs;
import DataAccess.PlaylistDataAccess;
import DataAccess.PlaylistSongsDataAccess;
import DataUpdates.PlaylistDataUpdate;
import Database.DatabaseConnection;
import Music.Song;

public class playlist extends AppCompatActivity {
    private DatabaseConnection connection = DatabaseConnection.getInstance(this);
    private PlaylistDataAccess playListDataAccess = new PlaylistDataAccess(connection);
    private PlaylistSongsDataAccess playlistSongsDataAccess = new PlaylistSongsDataAccess(connection);
    private PlaylistDataUpdate playlistDataUpdate = new PlaylistDataUpdate(connection);
    private RecyclerView recyclerView;
    private MediaPlayer mediaPlayer;
    private AdapterSongs adapterSongs;
    private TextView playlistTitle;
    private String title;
    private int playlistID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        connection.openDatabase();

        Intent playlistSelector = getIntent();
        playlistID = playlistSelector.getIntExtra("playlistID", -1);
        if (playlistID == -1) {
            Log.d("playlist", "Playlist ID:" + playlistID);
            throw new RuntimeException("Playlist ID not found");
        }

        recyclerView = findViewById(R.id.recyclerSongsSelector);

        playlistTitle = findViewById(R.id.playlistTitle);

        ImageView deletePlaylist = findViewById(R.id.deletePlaylist);
        ImageView editPlaylist = findViewById(R.id.editPlaylist);

        deletePlaylist.setOnClickListener(v -> {
            playlistDataUpdate.deletePlaylist(playlistID);
            Toast toast = Toast.makeText(this, "Playlist eliminada", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        });
        editPlaylist.setOnClickListener(v -> {
            Intent intent = new Intent(this, playlistEditor.class);
            intent.putExtra("playlistID", playlistID);
            startActivity(intent);
        });

        setPlaylistTitle();
        setSongsList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setPlaylistTitle();
        setSongsList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer = adapterSongs.getMediaPlayer();
        if (mediaPlayer != null) mediaPlayer.release();
    }

    private void setPlaylistTitle() {
        String title = playListDataAccess.getPlaylistTitle(playlistID);
        playlistTitle.setText(title);
    }
    private void setSongsList() {
        List<Song> songs = playlistSongsDataAccess.getSongsFromPlaylist(playlistID);
        adapterSongs = new AdapterSongs(songs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapterSongs);
    }
}