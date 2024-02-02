package Audio;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.os.StatFs;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.sql.Connection;

import AppContext.MyApplication;
import DataAccess.PreferencesDataAccess;
import Database.DatabaseConnection;

public class AudioRecorder extends Service {
    private MediaRecorder mediaRecorder;
    private final AudioManager manager = new AudioManager();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void startRecording(String outputFile) {
        if (manager.hasSufficientStorage()) {
            mediaRecorder = new MediaRecorder();

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(outputFile);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setAudioSamplingRate(manager.getPreferredSamplingRate());

            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mediaRecorder.start();
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