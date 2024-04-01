package Adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import Database.DatabaseConnection;
import Files.AudiosPaths;
import GameManagers.Challenges.ChallengesUpdater;

public class AdapterLoudSounds extends RecyclerView.Adapter<AdapterLoudSounds.ViewHolder> {
    private final List<List<Integer>> sounds;
    private final AudiosPaths audioFiles = new AudiosPaths();
    private final MediaPlayer mediaPlayer;
    private int soundsPlayer = 0;

    public AdapterLoudSounds(List<List<Integer>> sounds) {
        this.sounds = sounds;
        mediaPlayer = new MediaPlayer();
    }

    @NonNull
    @Override
    public AdapterLoudSounds.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_recordings, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterLoudSounds.ViewHolder holder, int position) {
        final String soundName = "Sonido " + (position + 1);
        holder.soundElement.setText(soundName);
        holder.soundElement.setOnClickListener(v -> {
            List<Integer> currentList = sounds.get(position);
            playSound(holder.itemView.getContext(), currentList);
        });
    }

    @Override
    public int getItemCount() {
        return sounds.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button soundElement;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            soundElement = itemView.findViewById(R.id.button_record);
        }
    }

    private void playSound(Context context, List<Integer> seconds) {
        String recordFilePath = audioFiles.getRecordingsPath();

        int startMillis = seconds.get(0) * 1000;
        int endSecond = seconds.get(seconds.size() - 1) + 1;
        int endMillis = endSecond * 1000;

        try {
            mediaPlayer.setDataSource(context, Uri.parse(recordFilePath));
            mediaPlayer.prepare();
            mediaPlayer.seekTo(startMillis);
            mediaPlayer.start();

            Handler handler = new Handler();
            handler.postDelayed(this::stopPlaying, endMillis - startMillis);

            soundsPlayer++;
            if (soundsPlayer == 3) {
                DatabaseConnection connection = DatabaseConnection.getInstance(context);
                ChallengesUpdater challengesUpdater = new ChallengesUpdater(connection);
                connection.openDatabase();
                challengesUpdater.updateLoudSoundsRecord();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
