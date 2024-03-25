package Adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.io.IOException;
import java.util.List;

import Files.AudiosPaths;

public class AdapterLoudSounds extends RecyclerView.Adapter<AdapterLoudSounds.ViewHolder> {
    private final List<List<Integer>> sounds;
    private final AudiosPaths audioFiles = new AudiosPaths();
    private final MediaPlayer mediaPlayer;

    public AdapterLoudSounds(List<List<Integer>> sounds) {
        this.sounds = sounds;
        mediaPlayer = new MediaPlayer();

        for (List<Integer> sound : sounds) {
            Log.d("AdapterLoudSounds", "Sound: " + sound);
        }
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
            Log.d("AdapterLoudSounds", "Clicked on sound: " + soundName);
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

        Log.d("AdapterLoudSounds", "Start: " + startMillis + " End: " + endMillis);

        try {
            mediaPlayer.setDataSource(context, Uri.parse(recordFilePath));
            mediaPlayer.prepare();
            mediaPlayer.seekTo(startMillis);
            mediaPlayer.start();

            Handler handler = new Handler();
            handler.postDelayed(this::stopPlaying, endMillis - startMillis);
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
