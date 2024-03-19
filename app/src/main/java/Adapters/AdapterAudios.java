package Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.PlaylistVisualizer;
import com.example.myapplication.R;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import AppContext.MyApplication;
import Database.DataUpdates.SongsDataUpdate;
import Database.DatabaseConnection;
import Music.PlaylistSongs;
import Models.Audio;
import Services.AudioPlayer;

public class AdapterAudios extends RecyclerView.Adapter<AdapterAudios.ViewHolder> {
    private final List<Audio> audios;
    private final Activity activity;
    private MediaPlayer mediaPlayer;

    public AdapterAudios(List<Audio> songs, Activity activity) {
        this.audios = songs;
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
        if (mediaPlayer != null) mediaPlayer.release();


        holder.setSongs(audios.get(position).getName());
        holder.button.setOnClickListener(v -> {
            mediaPlayer = new MediaPlayer();

            Intent intent = new Intent(activity, AudioPlayer.class);
            intent.putExtra("audios", (Serializable) audios);
            intent.putExtra("position", position);

            activity.startService(intent);
        });
    }
    @Override
    public int getItemCount() {
        return audios.size();
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
