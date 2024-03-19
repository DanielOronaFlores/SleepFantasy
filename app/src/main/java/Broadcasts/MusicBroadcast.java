package Broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import Services.AudioPlayer;

public class MusicBroadcast extends BroadcastReceiver {
    private MediaPlayer mediaPlayer;

    public MusicBroadcast() {
        mediaPlayer = AudioPlayer.getMediaPlayer();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            if (intent.getAction().equals("STOP_AUDIO_ACTION")) {
                Log.d("MusicBroadcast", "onReceive: STOP_AUDIO_ACTION" );
                stopAudio();
            }
        }
    }

    private void stopAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}