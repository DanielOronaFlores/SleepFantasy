package Recordings.AudioFilter;

import android.media.MediaMetadataRetriever;
import android.util.Log;

import org.jtransforms.fft.DoubleFFT_1D;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Files.AudiosFiles;
import Recordings.RecordingPreferences;

public class AudioFilter {
    private AudiosFiles audiosFiles = new AudiosFiles();
    private List<Integer> segmentosNoEliminados = new ArrayList<>();


    public void filterAudio() {
        try {
            double segmentDuration = 1.0;
            FileInputStream fileInputStream = new FileInputStream(audiosFiles.getPCMPath());

            String audioFilePath = audiosFiles.getRecordingsPath();
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(audioFilePath);

            float sampleRate = Float.parseFloat(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_SAMPLERATE));

            int samplesPerSegment = (int) (sampleRate * segmentDuration);
            double[] samples = new double[samplesPerSegment];

            int BUFFER_SAMPLE_SIZE = 2;
            byte[] buffer = new byte[samplesPerSegment * BUFFER_SAMPLE_SIZE];

            double totalSumOfSquares = 0;
            int totalSamples = 0;

            int section = 0;
            FFTFilter fftFilter = new FFTFilter((int) sampleRate, samplesPerSegment);
            RMSFilter rmsFilter = new RMSFilter(1);
            DoubleFFT_1D fft = new DoubleFFT_1D(samplesPerSegment);

            while (fileInputStream.read(buffer) != -1) {
                for (int i = 0; i < samplesPerSegment; i++) {
                    samples[i] = ((buffer[i * 2 + 1] << 8) | (buffer[i * 2] & 0xFF)) / 32768.0;
                }

                double sumOfSquares = Arrays.stream(samples).map(x -> x * x).sum();
                totalSumOfSquares += sumOfSquares;
                totalSamples += samplesPerSegment;

                fftFilter.applyFFT(samples);
                fftFilter.deleteHighFrequencies(samples, 300);

                double currentRMS = Math.sqrt(sumOfSquares / samplesPerSegment);
                double threshold = rmsFilter.calculateAmplitudeThreshold(totalSumOfSquares, totalSamples);
                rmsFilter.removeAudioLowerByRMS(samples, currentRMS, threshold);

                fft.realForward(samples);

                Log.d("Sample", "Sample: " + samples[0]);
                if (!Arrays.stream(samples).allMatch(x -> x == 0)) {
                    segmentosNoEliminados.add(section);
                }

                section++;
            }
            fileInputStream.close();

            for (Integer segmento : segmentosNoEliminados) {
                Log.d("Segmento", segmento.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
