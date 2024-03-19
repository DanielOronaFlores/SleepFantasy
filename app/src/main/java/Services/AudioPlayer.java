package Services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.R;

import java.io.IOException;
import java.util.List;

import AppContext.MyApplication;
import Broadcasts.MusicBroadcast;
import Database.DataUpdates.SongsDataUpdate;
import Database.DatabaseConnection;
import Models.Audio;
import Music.PlaylistSongs;

public class AudioPlayer extends Service {
    private List<Audio> audios;
    private static MediaPlayer mediaPlayer;
    private int position;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Notification notification = createNotification();
        startForeground(1, notification);

        audios = (List<Audio>) intent.getSerializableExtra("audios");
        position = intent.getIntExtra("position", 0);
        mediaPlayer = new MediaPlayer();

        new Thread(() -> ((Runnable) () -> {
            playAudioInLoop(position);
        }).run()).start();
        return super.onStartCommand(intent, flags, startId);
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void playAudioInLoop(int position) {
        if (mediaPlayer != null) mediaPlayer.release();

        String songName = audios.get(position).getName();
        Log.d("AdapterAudios", "Audio Playing: " + songName);
        int isCreatedBySystem = audios.get(position).getIbBySystem();

        if (isCreatedBySystem == 1) {
            int songID = selectSong(songName);
            playSongCreatedBySystem(songID, MyApplication.getAppContext());
        } else {
            playSongCreatedByUser(songName);
        }
        mediaPlayer.setOnCompletionListener(mp -> resetAudioLoop());
    }

    private void playSongCreatedBySystem(int songID, Context context) {
        mediaPlayer = MediaPlayer.create(context, songID);
        mediaPlayer.start();
    }

    private void playSongCreatedByUser(String audioName) {
        mediaPlayer = new MediaPlayer();
        String audioPath = "/sdcard/Music/" + audioName;

        try {
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            DatabaseConnection connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
            SongsDataUpdate songsDataUpdate = new SongsDataUpdate(connection);
            songsDataUpdate.deleteSong(audioName);

            Toast.makeText(MyApplication.getAppContext(), "Canción no encontrada. Eliminando de la PlayList", Toast.LENGTH_SHORT).show();
        }
    }

    private int selectSong(String songName) {
        PlaylistSongs playlistSongs = new PlaylistSongs();
        return playlistSongs.getResourceId(songName);
    }

    private void resetAudioLoop() {
        position++;
        if (position < audios.size()) {
            playAudioInLoop(position);
        } else {
            position = 0;
            playAudioInLoop(position);
        }
    }


    private Notification createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "audio_player_channel")
                .setSmallIcon(R.drawable.placeholder_cono)
                .setContentTitle("Sleep Fantasy Audio Player")
                .setContentText("Reproduciendo audio...")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent stopAudioIntent = new Intent(this, MusicBroadcast.class);
        stopAudioIntent.setAction("STOP_AUDIO_ACTION");
        PendingIntent stopAudioPendingIntent = PendingIntent.getBroadcast(this, 0, stopAudioIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.addAction(R.drawable.placeholder_cono, "Detener audio", stopAudioPendingIntent);

        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                .setHintHideIcon(true);

        builder.extend(wearableExtender);

        return builder.build();
    }
}