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
import Database.DataAccess.PlaylistDataAccess;
import Database.DataAccess.PlaylistAudiosDataAccess;
import Database.DataUpdates.PlaylistDataUpdate;
import Database.DataUpdates.PlaylistAudiosDataUpdate;
import Database.DatabaseConnection;
import GameManagers.Challenges.ChallengesUpdater;
import Models.Audio;
import Styles.Themes;

public class PlaylistEditor extends AppCompatActivity {
    private final DatabaseConnection connection = DatabaseConnection.getInstance(this);
    private final PlaylistAudiosDataAccess PlaylistAudiosDataAccess = new PlaylistAudiosDataAccess(connection);
    private final PlaylistDataAccess playlistDataAccess = new PlaylistDataAccess(connection);
    private final PlaylistDataUpdate playlistDataUpdate = new PlaylistDataUpdate(connection);
    private final PlaylistAudiosDataUpdate PlaylistAudiosDataUpdate = new PlaylistAudiosDataUpdate(connection);
    private RecyclerView recyclerView;
    private EditText playlistName;
    private List<Audio> Audios, selectedAudios;
    private boolean addAudiosMode;
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

        Button addAudios = findViewById(R.id.addAudios);
        addAudios.setOnClickListener(v -> showNotInPlaylist(playlistID));

        Button deleteAudios = findViewById(R.id.deleteAudios);
        deleteAudios.setOnClickListener(v -> showInPlaylist(playlistID));

        Button editPlaylist = findViewById(R.id.editPlaylist);
        editPlaylist.setOnClickListener(v -> updatePlaylist());

        recyclerView = findViewById(R.id.recyclerEditAudios);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTheme();
    }

    private void generateAudiosList(List<Audio> Audios) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new AdapterPlaylistCreator(Audios));
    }

    private void showInPlaylist(int playlistID) {
        addAudiosMode = false;
        Audios = PlaylistAudiosDataAccess.getAudiosFromPlaylist(playlistID);
        generateAudiosList(Audios);
    }
    private void showNotInPlaylist(int playlistID) {
        addAudiosMode = true;
        Audios = PlaylistAudiosDataAccess.getNotAudiosFromPlaylist(playlistID);
        generateAudiosList(Audios);
    }

    private void deleteAudiosFromPlaylist() {
        for (Audio audio: selectedAudios) {
            if (audio.getIsCreatedBySystem() == 0) {
                ChallengesUpdater challengesUploader = new ChallengesUpdater();
                challengesUploader.updateDeleteAudioRecord();
            }
            PlaylistAudiosDataUpdate.deleteAudioFromPlaylist(playlistID, audio.getId());
        }
    }
    private void addAudiosToPlaylist() {
        for (Audio audio: selectedAudios) {
            PlaylistAudiosDataUpdate.addaudioToPlaylist(playlistID, audio.getId());
        }
    }

    private boolean isEditingAudios() {
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
            if (isEditingAudios()) {
                selectedAudios = ((AdapterPlaylistCreator) Objects.requireNonNull(recyclerView.getAdapter())).getSelectedAudios();
                if (addAudiosMode) {
                    addAudiosToPlaylist();
                    Toast.makeText(this, "Audios agregados a la playlist", Toast.LENGTH_SHORT).show();
                } else {
                    deleteAudiosFromPlaylist();
                    Toast.makeText(this, "Audios eliminados de la playlist", Toast.LENGTH_SHORT).show();
                    if (selectedAudios.size() == Audios.size()) {
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

    private void setTheme() {
        Themes.setBackgroundColor(this, findViewById(R.id.playlistEditor));
        Themes.setButtonTheme(this, findViewById(R.id.addAudios));
        Themes.setButtonTheme(this, findViewById(R.id.deleteAudios));
        Themes.setButtonTheme(this, findViewById(R.id.editPlaylist));
    }
}