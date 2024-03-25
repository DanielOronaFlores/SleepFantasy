package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import Adapters.AdapterAddAudios;
import Database.DataAccess.PlaylistDataAccess;
import Database.DataAccess.SongsDataAccess;
import Database.DataUpdates.PlaylistDataUpdate;
import Database.DataUpdates.PlaylistSongsDataUpdate;
import Database.DataUpdates.SongsDataUpdate;
import Database.DatabaseConnection;

public class AddAudios extends AppCompatActivity {
    private final DatabaseConnection connection = DatabaseConnection.getInstance(this);
    private final SongsDataUpdate songsDataUpdate = new SongsDataUpdate(connection);
    private final SongsDataAccess songsDataAccess = new SongsDataAccess(connection);
    private final PlaylistDataUpdate playlistDataUpdate = new PlaylistDataUpdate(connection);
    private final PlaylistDataAccess playlistDataAccess = new PlaylistDataAccess(connection);
    private final PlaylistSongsDataUpdate playlistSongsDataUpdate = new PlaylistSongsDataUpdate(connection);
    private RecyclerView recyclerView;
    private Button addAudios;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_audios);

        recyclerView = findViewById(R.id.newAudiosRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new AdapterAddAudios());

        addAudios = findViewById(R.id.addSelectedAudios);
        addAudios.setOnClickListener(v -> addSelectedAudios());
    }

    @Override
    protected void onStart() {
        super.onStart();
        connection.openDatabase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void addSelectedAudios() {
        List<String> selectedAudios = ((AdapterAddAudios) Objects.requireNonNull(recyclerView.getAdapter())).getSelectedAudios();
        if (!selectedAudios.isEmpty()) {
            if (!playlistDataAccess.isPlaylistCreated("Audio Propios")) {
                playlistDataUpdate.createPlaylist("Audio Propios", true);
            }

            int playListID = playlistDataAccess.getPlaylistId("Audio Propios");

            for (String audio : selectedAudios) {
                songsDataUpdate.addSong(audio);
                int songID = songsDataAccess.getSongID(audio);
                playlistSongsDataUpdate.addSongToPlaylist(playListID, songID);
            }
            Toast.makeText(this, "AUDIOS AGREGADOS CORRECTAMENTE", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}