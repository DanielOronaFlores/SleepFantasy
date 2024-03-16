package AudioFilter;

import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Files.AudiosPaths;
import Serializers.Serializer;
import Models.Sound;

public class AudioFilter {
    private final AudiosPaths audiosFiles = new AudiosPaths();
    private final List<Sound> soundsList = new ArrayList<>();

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

            FFTFilter fftFilter = new FFTFilter((int) sampleRate, samplesPerSegment);
            RMSFilter rmsFilter = new RMSFilter(1);

            int second = 0;
            while (fileInputStream.read(buffer) != -1) {
                second++;

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

                if (second % 2 != 0) {
                    Log.d("FFT", "-----" + second / 2 + "-----");
                    Log.d("FFT", Arrays.toString(samples));
                    if (samples[0] != 0) {
                        soundsList.add(new Sound(second / 2));
                    }
                }
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Serializer serializer = new Serializer();
        serializer.serializeSoundsToXML(soundsList);
    }
}
