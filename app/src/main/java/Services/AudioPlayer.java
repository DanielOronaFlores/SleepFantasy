package Services;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

import AppContext.MyApplication;
import Broadcasts.MusicBroadcast;
import Database.DataAccess.PreferencesDataAccess;
import Database.DataAccess.SongsDataAccess;
import Database.DataUpdates.PlaylistSongsDataUpdate;
import Database.DataUpdates.SongsDataUpdate;
import Database.DatabaseConnection;
import Models.Audio;
import Models.PlaylistAudios;

public class AudioPlayer extends Service {
    private final DatabaseConnection connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
    private final PreferencesDataAccess preferencesDataAccess = new PreferencesDataAccess(connection);
    private List<Audio> audios;
    private static MediaPlayer mediaPlayer;
    private int position;
    private Activity activity;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        connection.openDatabase();

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        long timer = preferencesDataAccess.getTimerDuration();
        if (timer > 0) {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                stopSelf();
            }, timer);
        }

        audios = (List<Audio>) intent.getSerializableExtra("audios");
        position = intent.getIntExtra("position", 0);

        mediaPlayer = new MediaPlayer();

        new Thread(() -> playAudioInLoop(position)).start();

        return super.onStartCommand(intent, flags, startId);
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void playAudioInLoop(int position) {
        if (mediaPlayer != null) mediaPlayer.release();

        String songName = audios.get(position).getName();
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

    @SuppressLint("ForegroundServiceType")
    private void playSongCreatedByUser(String audioName) {
        mediaPlayer = new MediaPlayer();
        String audioPath = "/sdcard/Music/" + audioName;

        File audioFile = new File(audioPath);
        if (audioFile.exists()) {
            Notification notification = createNotification();
            startForeground(1, notification);

            try {
                mediaPlayer.setDataSource(audioPath);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(() -> Toast.makeText(getApplicationContext(), "Audio no encontrando. Eliminando de Playlists", Toast.LENGTH_SHORT).show());

            DatabaseConnection connection = DatabaseConnection.getInstance(MyApplication.getAppContext());

            SongsDataUpdate songsDataUpdate = new SongsDataUpdate(connection);
            songsDataUpdate.deleteSong(audioName);

            SongsDataAccess songsDataAccess = new SongsDataAccess(connection);
            int songID = songsDataAccess.getSongID(audioName);

            PlaylistSongsDataUpdate playlistSongsDataUpdate = new PlaylistSongsDataUpdate(connection);
            playlistSongsDataUpdate.deleteAudio(songID);

            recreateActivity();

            stopSelf();
        }
    }

    private int selectSong(String songName) {
        PlaylistAudios playlistSongs = new PlaylistAudios();
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
        NotificationChannel channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel("audio_player_channel", "Audio Player Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

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

    private void recreateActivity() {
        Intent intent = new Intent("RECREAR_ACTIVIDAD");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}