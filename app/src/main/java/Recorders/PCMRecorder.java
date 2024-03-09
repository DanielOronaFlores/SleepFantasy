package Recorders;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import androidx.core.app.ActivityCompat;

import java.io.FileOutputStream;
import java.io.IOException;

import AppContext.MyApplication;
import Files.AudiosPaths;
import Preferences.RecordingPreferences;

public class PCMRecorder {
    private final RecordingPreferences recordingPreferences = new RecordingPreferences();
    private final AudiosPaths audiosFiles = new AudiosPaths();
    private boolean isRecording = false;
    private AudioRecord audioRecord;


    public void startRecording() {
        int RECORDER_SAMPLE_RATE = recordingPreferences.getPreferredSamplingRate();
        int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
        int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

        int bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLE_RATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING);

        if (ActivityCompat.checkSelfPermission(MyApplication.getAppContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) return;

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLE_RATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING,
                bufferSize);

        audioRecord.startRecording();
        isRecording = true;

        Thread recordingThread = new Thread(() -> writeAudioDataToFile(RECORDER_SAMPLE_RATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING));

        recordingThread.start();
    }

    private void writeAudioDataToFile(int RECORDER_SAMPLE_RATE, int RECORDER_CHANNELS, int RECORDER_AUDIO_ENCODING) {
        byte[] data = new byte[AudioRecord.getMinBufferSize(
                RECORDER_SAMPLE_RATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING)];

        String filePath = audiosFiles.getPCMPath();

        try {
            FileOutputStream os = new FileOutputStream(filePath);
            while (isRecording) {
                int read = audioRecord.read(data, 0, data.length);
                if (read != AudioRecord.ERROR_INVALID_OPERATION) os.write(data);
            }
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        isRecording = false;
        audioRecord.stop();
        audioRecord.release();
    }
}