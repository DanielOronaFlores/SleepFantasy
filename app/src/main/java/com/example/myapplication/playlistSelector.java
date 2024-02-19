package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class playlistSelector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list_selector);

        Button selectNature = findViewById(R.id.selectNature);
        Button selectClassicMusic = findViewById(R.id.selectClassicMusic);
        Button selectWhiteNoise = findViewById(R.id.selectWhiteNoise);
        Button selectAmbientNoise = findViewById(R.id.selectAmbientNoise);

        selectNature.setOnClickListener(view -> selectPlaylist(1));
        selectClassicMusic.setOnClickListener(view -> selectPlaylist(2));
        selectWhiteNoise.setOnClickListener(view -> selectPlaylist(3));
        selectAmbientNoise.setOnClickListener(view -> selectPlaylist(4));
    }

    private void selectPlaylist(int playlistID) {
        Intent intent = new Intent(this, playlist.class);
        intent.putExtra("playlistID", playlistID);
        startActivity(intent);
    }
}