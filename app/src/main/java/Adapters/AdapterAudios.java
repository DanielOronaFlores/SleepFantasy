package Adapters;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.io.IOException;
import java.util.List;

import AppContext.MyApplication;
import Database.DataUpdates.SongsDataUpdate;
import Database.DatabaseConnection;
import Files.AudiosPaths;
import Music.PlaylistSongs;
import Models.Song;

public class AdapterAudios extends RecyclerView.Adapter<AdapterAudios.ViewHolder> {
    private final List<Song> songs;
    private final Activity activity;
    private MediaPlayer mediaPlayer;

    public AdapterAudios(List<Song> songs, Activity activity) {
        this.songs = songs;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterAudios.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_song_selector, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AdapterAudios.ViewHolder holder, int position) {
        holder.setSongs(songs.get(position).getName());
        holder.button.setOnClickListener(v -> {
            String songName = songs.get(position).getName();
            int isCreatedBySystem = songs.get(position).getIbBySystem();
            Log.d("AdapterSongs", "CreatedBySystem " + isCreatedBySystem);

            if (isCreatedBySystem == 1) {
                int songID = selectSong(songName);
                playSongCreatedBySystem(songID, holder.itemView.getContext());
            } else {
                playSongCreatedByUser(songName);
            }
        });
    }
    @Override
    public int getItemCount() {
        Log.d("AdapterSongs", "Song size:" + songs.size());
        return songs.size();
    }

    private int selectSong(String songName) {
        PlaylistSongs playlistSongs = new PlaylistSongs();
        return playlistSongs.getResourceId(songName);
    }
    private void playSongCreatedBySystem(int songID, Context context) {
        Log.d("AdapterSongs", "Obtenido ID: " + songID);
        if (mediaPlayer != null) mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(context, songID);
        mediaPlayer.start();
    }
    private void playSongCreatedByUser(String audioName) {
        if (mediaPlayer != null) mediaPlayer.release();
        mediaPlayer = new MediaPlayer();
        String audioPath = "/sdcard/Music/" + audioName;
        Log.d("AdapterSongs", "Audio path: " + audioPath);

        try {
            assert mediaPlayer != null;
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            DatabaseConnection connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
            SongsDataUpdate songsDataUpdate = new SongsDataUpdate(connection);
            songsDataUpdate.deleteSong(audioName);

            Toast.makeText(activity, "Canción no encontrada. Eliminando de la PlayList", Toast.LENGTH_SHORT).show();
            activity.recreate();
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.selectSong);
        }
        public void setSongs(String s) {
            button.setText(s);
        }
    }
}
