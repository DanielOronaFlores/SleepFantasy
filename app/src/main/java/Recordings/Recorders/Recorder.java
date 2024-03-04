package Recordings.Recorders;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

import Files.AudiosFiles;
import Recordings.RecordingPreferences;

public class Recorder {
    private final RecordingPreferences recordingPreferences = new RecordingPreferences();
    private final AudiosFiles audiosFiles = new AudiosFiles();
    private MediaRecorder mediaRecorder = new MediaRecorder();

    public void startRecording() {
        String outputFile = audiosFiles.getRecordingsPath();

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(outputFile);
        mediaRecorder.setAudioSamplingRate(recordingPreferences.getPreferredSamplingRate());
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            Log.d("AudioRecorder", "Recording started");
        } catch (IOException e) {
            Log.d("AudioRecorder", "prepare() failed");
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
