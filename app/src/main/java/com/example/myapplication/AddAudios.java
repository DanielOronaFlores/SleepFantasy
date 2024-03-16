package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import Adapters.AdapterAddAudios;
import Database.DataUpdates.SongsDataUpdate;
import Database.DatabaseConnection;

public class AddAudios extends AppCompatActivity {
    private final DatabaseConnection connection = DatabaseConnection.getInstance(this);
    private final SongsDataUpdate songsDataUpdate = new SongsDataUpdate(connection);
    private RecyclerView recyclerView;
    private Button addAudios;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_audios);


        connection.openDatabase();

        recyclerView = findViewById(R.id.newAudiosRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new AdapterAddAudios());

        addAudios = findViewById(R.id.addSelectedAudios);
        addAudios.setOnClickListener(v -> addSelectedAudios());
    }

    private void addSelectedAudios() {
        List<String> selectedAudios = ((AdapterAddAudios) Objects.requireNonNull(recyclerView.getAdapter())).getSelectedAudios();
        if (!selectedAudios.isEmpty()) {
            for (String audio : selectedAudios) {
                songsDataUpdate.addSong(audio);
            }
        }
    }
}