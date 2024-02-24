package AudioRecorder;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

import Files.StorageManager;

public class AudioRecorder {
    private MediaRecorder mediaRecorder;
    private final AudioManager manager = new AudioManager();
    private final StorageManager storageManager = new StorageManager();

    public void startRecording(String outputFile) {
        if (storageManager.hasSufficientStorage()) {
            mediaRecorder = new MediaRecorder();

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(outputFile);
            mediaRecorder.setAudioSamplingRate(manager.getPreferredSamplingRate());
            Log.d("AudioRecorder", "Recording started");
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IOException e) {
                Log.d("AudioRecorder", "prepare() failed");
                e.printStackTrace();
            }
        } else {
            manager.notifyLowStorage();
        }
    }

    private void createSoundsList() {

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