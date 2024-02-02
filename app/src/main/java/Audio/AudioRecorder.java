package Audio;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.IOException;

public class AudioRecorder extends Service {
    private final MediaRecorder mediaRecorder = new MediaRecorder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //Metodos de la clase
    public void startRecording(String outputFile) {
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(outputFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
    }

    public void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
    }
}
