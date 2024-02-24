package AudioRecorder;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

import Files.StorageManager;
import Notifications.Notifications;

public class AudioRecorder {
    private MediaRecorder mediaRecorder = new MediaRecorder();
    private final RecordingPreferences manager = new RecordingPreferences();
    private final StorageManager storageManager = new StorageManager();

    public void startRecording(String outputFile) {
        if (storageManager.hasSufficientStorage()) {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(outputFile);
            mediaRecorder.setAudioSamplingRate(manager.getPreferredSamplingRate());
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                Log.d("AudioRecorder", "Recording started");
            } catch (IOException e) {
                Log.d("AudioRecorder", "prepare() failed");
                e.printStackTrace();
            }
        } else {
            Notifications notifications = new Notifications();
            notifications.showLowStorageNotification();
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