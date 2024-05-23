package Recorders;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

import Files.AudiosPaths;
import Recorders.Preferences.RecordingPreferences;

public class Recorder {
    private final RecordingPreferences recordingPreferences = new RecordingPreferences();
    private MediaRecorder mediaRecorder;

    public void startRecording() {
        mediaRecorder = new MediaRecorder();
        String outputFile = AudiosPaths.getRecordings3GPPath();

        File file = new File(outputFile);
        System.out.println("File exists: " + file.exists());
        if (file.exists()) {
            System.out.println("Borrando archivo existente");
            file.delete();
        }

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
    public boolean isRecording() {
        System.out.println("is Recording: " + (mediaRecorder != null));
        return mediaRecorder != null;
    }
}
