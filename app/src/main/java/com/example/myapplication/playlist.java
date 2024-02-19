package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import DataAccess.PlayListsDataAccess;
import DataAccess.SongsDataAccess;
import Database.DatabaseConnection;

public class playlist extends AppCompatActivity {
    DatabaseConnection connection;
    MediaPlayer mediaPlayer = new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();
        PlayListsDataAccess playListsDataAccess = new PlayListsDataAccess(connection);
        SongsDataAccess songsDataAccess = new SongsDataAccess(connection);

        Intent intent = getIntent();
        int playlistID = intent.getIntExtra("playlistID", -1);
        if (playlistID == -1) {
            Log.d("playlist", "Playlist ID:" + playlistID);
            throw new RuntimeException("Playlist ID not found");
        }

        TextView playlistTitle = findViewById(R.id.playlistTitle);
        String title = playListsDataAccess.getPlaylistTitle(playlistID);
        playlistTitle.setText(title);

        Button song1 = findViewById(R.id.song1);
        Button song2 = findViewById(R.id.song2);
        Button song3 = findViewById(R.id.song3);

        int[] music = getMusic(playlistID);

        song1.setOnClickListener(v -> playMusic(music[0]));
        song2.setOnClickListener(v -> playMusic(music[1]));
        song3.setOnClickListener(v -> playMusic(music[2]));

        int prefixID = getPrefixID(playlistID);
        song1.setText(songsDataAccess.getSongTitle(prefixID + 1));
        song2.setText(songsDataAccess.getSongTitle(prefixID + 2));
        song3.setText(songsDataAccess.getSongTitle(prefixID + 3));
    }

    private static final int[] MUSIC_NATURE = {
            R.raw.music_nature_rain,
            R.raw.music_nature_stream,
            R.raw.music_nature_waves,
    };
    private static final int[] MUSIC_CLASSICAL = {
            R.raw.music_classical_adagio,
            R.raw.music_classical_clairdelune,
            R.raw.music_classical_pavane,
    };
    private static final int[] MUSIC_WHITENOISE = {
            R.raw.music_whitenoise_brown,
            R.raw.music_whitenoise_pink,
            R.raw.music_whitenoise_white,
    };
    private static final int[] MUSIC_AMBIENT = {
            R.raw.music_ambient_cafe,
            R.raw.music_ambient_locomotive,
            R.raw.music_ambient_office,
    };

    private int[] getMusic(int playlistID) {
        switch (playlistID) {
            case 1:
                return MUSIC_NATURE;
            case 2:
                return MUSIC_CLASSICAL;
            case 3:
                return MUSIC_WHITENOISE;
            case 4:
                return MUSIC_AMBIENT;
            default:
                throw new RuntimeException("Playlist ID not found");
        }
    }
    private int getPrefixID(int playlistID) { //You must to add 1
        final int SONGS_PER_PLAYLIST = 3;
        return (SONGS_PER_PLAYLIST * playlistID) - SONGS_PER_PLAYLIST;
    }

    private void playMusic(int musicID) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getResources().openRawResourceFd(musicID));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.closeDatabase();
        mediaPlayer.release();
    }
}