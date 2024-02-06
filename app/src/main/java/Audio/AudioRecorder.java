package Audio;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class AudioRecorder {
    private MediaRecorder mediaRecorder;
    private final AudioManager manager = new AudioManager();

    public void startRecording(String outputFile) {
        if (manager.hasSufficientStorage()) {
            mediaRecorder = new MediaRecorder();

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(outputFile);
            mediaRecorder.setAudioSamplingRate(manager.getPreferredSamplingRate());

            try {
                Log.d("AudioRecorder", "startRecording: Preparing mediaRecorder");
                mediaRecorder.prepare();
                mediaRecorder.start();
                Log.d("AudioRecorder", "startRecording: Recording started");
            } catch (IOException e) {
                Log.e("AudioRecorder", "startRecording: prepare() failed");
                e.printStackTrace();
            }
        } else {
            manager.notifyLowStorage();
        }
    }

    public void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }

        if (manager.shouldSaveRecording()) manager.transferRecordingToSmartphone();
    }
}