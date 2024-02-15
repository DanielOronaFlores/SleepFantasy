package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chibde.visualizer.LineVisualizer;

import org.jtransforms.fft.DoubleFFT_1D;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import DataAccess.PreferencesDataAccess;
import Database.DatabaseConnection;
import Files.AudiosFiles;
import SoundEcofilter.AudioSamplesConverter;
import SoundEcofilter.BytesConverter;
import SoundEcofilter.FrequencyFilter;
import SoundEcofilter.RootMeanSquare;

public class recordsVisualizer extends AppCompatActivity {
    TextView textView;
    MediaPlayer mediaPlayer = new MediaPlayer();
    AudiosFiles audiosFiles = new AudiosFiles();
    Button calidadButton, elimnarButton;
    DatabaseConnection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_visualizer);

        connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();

        LineVisualizer lineVisualizer = findViewById(R.id.visualizer);
        //mediaPlayer.setDataSource(filePath);
        //mediaPlayer.prepare();
        mediaPlayer = MediaPlayer.create(this, R.raw.c);

        lineVisualizer.setColor(ContextCompat.getColor(this, R.color.black));
        lineVisualizer.setStrokeWidth(5);
        lineVisualizer.setPlayer(mediaPlayer.getAudioSessionId());

        textView = findViewById(R.id.bttn);
        textView.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        });

        elimnarButton = findViewById(R.id.btn_eliminar);
        elimnarButton.setOnClickListener(v -> deleteRecording());

        //test();
    }

    private void deleteRecording() {
        File file = new File(audiosFiles.getRecordingsPath());
        if (file.exists()) {
            if (file.delete()) Toast.makeText(this, "Audio Eliminado", Toast.LENGTH_SHORT).show();
            else Toast.makeText(this, "No se pudo eliminar el archivo", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No hay audio disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private void processAndSaveAudio() throws IOException {
        String inputPath = "/storage/emulated/0/Android/data/com.example.myapplication/files/Music/c.wav";
        String outputPath = "/storage/emulated/0/Android/data/com.example.myapplication/files/Music/wonka.wav";

        final double segmentDuration = 1.0; // segundos

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(inputPath);

        float sampleRate = Float.parseFloat(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_SAMPLERATE));
        int samplesPerSegment = (int) (sampleRate * segmentDuration);

        MediaExtractor extractor = new MediaExtractor();
        extractor.setDataSource(inputPath);
        int trackIndex = 0;
        extractor.selectTrack(trackIndex);

        int bufferSize = AudioTrack.getMinBufferSize((int) sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, (int) sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);

        byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BytesConverter bytesConverter = new BytesConverter();
        RootMeanSquare rms;
        FrequencyFilter filter;

        double totalSumOfSquares = 0;
        int totalSamples = 0;

        audioTrack.play();

        while (true) {
            int sampleSize = extractor.readSampleData(ByteBuffer.wrap(buffer), 0);
            if (sampleSize < 0) break;

            double[] samples = new double[samplesPerSegment];
            ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asDoubleBuffer().get(samples);

            rms = new RootMeanSquare(1.0);
            double sumOfSquares = rms.calculateRMS(samples);
            totalSumOfSquares += sumOfSquares;
            totalSamples += samplesPerSegment;

            DoubleFFT_1D fft = new DoubleFFT_1D(samplesPerSegment);
            fft.realForward(samples);

            filter = new FrequencyFilter(samplesPerSegment, samples, sampleRate);
            filter.filterLowerFrequenciesByHertz(300);
            samples = filter.getSamples();

            double currentRMS = Math.sqrt(sumOfSquares / samplesPerSegment);
            double threshold = rms.calculateAmplitudeThreshold(totalSumOfSquares, totalSamples);
            samples = rms.removeAudioLowerByRMS(samples, currentRMS, threshold);

            fft.realInverse(samples, true);

            byte[] resultBuffer = bytesConverter.samplesToBytes(samples);
            byteArrayOutputStream.write(resultBuffer);

            audioTrack.write(buffer, 0, sampleSize);
            extractor.advance(); // Move to the next block of data
        }

        audioTrack.stop();
        audioTrack.release();
        extractor.release();

        byteArrayOutputStream.close();

        // Save the processed audio to a new file
        FileOutputStream outputStream = new FileOutputStream(outputPath);
        outputStream.write(byteArrayOutputStream.toByteArray());
        outputStream.close();

        Log.d("TAG", "Audio processing and saving completed.");
    }

    private static void processAudio() throws IOException {
        String inputPath = "/storage/emulated/0/Android/data/com.example.myapplication/files/Music/c.wav";
        String outputPath = "/storage/emulated/0/Android/data/com.example.myapplication/files/Music/new.wav";

        MediaExtractor extractor = new MediaExtractor();
        extractor.setDataSource(inputPath);

        MediaFormat format = extractor.getTrackFormat(0);
        int sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        int channelCount = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
        int bufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);

        byte[] buffer = new byte[bufferSize];
        FileInputStream inputStream = new FileInputStream(inputPath);
        FileOutputStream outputStream = new FileOutputStream(outputPath);

        audioTrack.play();

        int bytesRead;
        while ((bytesRead = inputStream.read(buffer, 0, bufferSize)) > 0) {
            audioTrack.write(buffer, 0, bytesRead);
            outputStream.write(buffer, 0, bytesRead);
        }

        audioTrack.stop();
        audioTrack.release();

        inputStream.close();
        outputStream.close();

        Log.d("TAG", "Audio processing completed.");
    }

    private void createSoundsList() throws IOException {
        String audioFilePath = "/storage/emulated/0/Android/data/com.example.myapplication/files/Music/c.wav"; // Ruta del archivo de audio

        final double segmentDuration = 1.0; // segundos
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(audioFilePath);

        float sampleRate = Float.parseFloat(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_SAMPLERATE));
        Log.d("TAG", "SAMPLE RATE: " + sampleRate);
        int samplesPerSegment = (int) (sampleRate * segmentDuration);
        Log.d("TAG", "SAMPLES PER SEGMENT: " + samplesPerSegment);
        double[] samples = new double[samplesPerSegment];
        Log.d("TAG", "SAMPLES LENGHT: " + samples.length);

        int BUFFER_SIZE = 2; // Tamaño del búfer en bytes
        byte[] buffer = new byte[samplesPerSegment * BUFFER_SIZE];
        Log.d("TAG", "BUFFER LENGHT: " + buffer.length);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BytesConverter bytesConverter = new BytesConverter();

        RootMeanSquare rms;
        FrequencyFilter filter;

        double totalSumOfSquares = 0;
        int totalSamples = 0;

        // Crear el objeto MediaExtractor y configurarlo con el archivo de audio
        MediaExtractor extractor = new MediaExtractor();
        extractor.setDataSource(audioFilePath);
        int trackIndex = 0;
        extractor.selectTrack(trackIndex);

        AudioSamplesConverter audioSamplesConverter = new AudioSamplesConverter(samplesPerSegment, buffer, samples);
        int i = 0;
        rms = new RootMeanSquare(1.0);


        while (true) {
            int sampleSize = extractor.readSampleData(ByteBuffer.wrap(buffer), 0);
            if (sampleSize < 0) break;
            i++;
            Log.d("ROUND", "createSoundsList: " + i);

            audioSamplesConverter.convertSamples();
            samples = audioSamplesConverter.getSamples();
            Log.d("TAG", "SAMPLES LENGHT: " + samples.length);
            double sumOfSquares = rms.calculateRMS(samples);
            Log.d("TAG", "SUM OF SQUARES: " + sumOfSquares);
            totalSumOfSquares += sumOfSquares;
            totalSamples += samplesPerSegment;

            DoubleFFT_1D fft = new DoubleFFT_1D(samplesPerSegment);
            fft.realForward(samples);

            filter = new FrequencyFilter(samplesPerSegment, samples, sampleRate);
            filter.filterLowerFrequenciesByHertz(300);
            samples = filter.getSamples();
            Log.d("TAG", "FILTERED SAMPLES LENGHT: " + samples.length);

            double currentRMS = Math.sqrt(sumOfSquares / samplesPerSegment);
            double threshold = rms.calculateAmplitudeThreshold(totalSumOfSquares, totalSamples);
            samples = rms.removeAudioLowerByRMS(samples, currentRMS, threshold);
            Log.d("TAG", "FILTERED SAMPLES LENGHT RMS: " + samples.length);

            fft.realInverse(samples, true);

            byte[] resultBuffer = bytesConverter.samplesToBytes(samples);
            byteArrayOutputStream.write(resultBuffer);
            Log.d("TAG", "BYTE ARRAY OUTPUT STREAM LENGHT: " + byteArrayOutputStream.size());

            extractor.advance(); // Mover al siguiente bloque de datos
        }

        // Crear el archivo de salida
        String outputFilePath = "/storage/emulated/0/Android/data/com.example.myapplication/files/Music/output.wav";
        File outputFile = new File(outputFilePath);

        try {
            if (outputFile.createNewFile()) {
                Log.d("TAG", "Archivo de salida creado correctamente");
            } else {
                Log.d("TAG", "El archivo de salida ya existe, se sobrescribirá");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Configurar el objeto AudioTrack
        int bufferSize = AudioTrack.getMinBufferSize((int) sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, (int) sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);

        // Inicializar el objeto FileOutputStream para escribir en el archivo de salida
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

        // Inicializar el objeto MediaCodec para codificar los datos PCM a un formato de archivo de audio (como WAV)
        // (Debes implementar esta parte según tus necesidades específicas)

        // Reproducir los datos de muestra con AudioTrack
        audioTrack.play();
        audioTrack.write(buffer, 0, buffer.length);

        // Detener y liberar recursos de AudioTrack
        audioTrack.stop();
        audioTrack.release();

        // Cerrar el FileOutputStream
        fileOutputStream.close();
    }
}