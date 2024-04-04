package Recorders;

import android.media.MediaRecorder;

import java.io.IOException;

import Files.AudiosPaths;
import Recorders.Preferences.RecordingPreferences;

public class Recorder {
    private final RecordingPreferences recordingPreferences = new RecordingPreferences();
    private final AudiosPaths audiosFiles = new AudiosPaths();
    private MediaRecorder mediaRecorder = new MediaRecorder();

    public void startRecording() {
        String outputFile = audiosFiles.getRecordings3GPPath();

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(outputFile);
        mediaRecorder.setAudioSamplingRate(recordingPreferences.getPreferredSamplingRate());
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            System.out.println("Iniciando grbacion .3GP");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
