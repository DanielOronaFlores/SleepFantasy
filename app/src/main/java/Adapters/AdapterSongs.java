package Adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

import Music.PlaylistSongs;
import Models.Song;

public class AdapterSongs extends RecyclerView.Adapter<AdapterSongs.ViewHolder> {
    private final List<Song> songs;
    private MediaPlayer mediaPlayer;

    public AdapterSongs(List<Song> songs) {
        this.songs = songs;
    }

    @NonNull
    @Override
    public AdapterSongs.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_song_selector, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AdapterSongs.ViewHolder holder, int position) {
        holder.setSongs(songs.get(position).getName());
        holder.button.setOnClickListener(v -> {
            String songName = songs.get(position).getName();
            int songID = selectSong(songName);
            playSong(songID, holder.itemView.getContext());
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
    private void playSong(int songID, Context context) {
        Log.d("AdapterSongs", "Obtenido ID: " + songID);
        if (mediaPlayer != null) mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(context, songID);
        mediaPlayer.start();
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
