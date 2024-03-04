package Recordings.AudioFilter;

import android.util.Log;

import java.util.Arrays;

public class RMSFilter {
    private final int thresholdMultiplier;
    public RMSFilter(int thresholdMultiplier) {
        this.thresholdMultiplier = thresholdMultiplier;
    }

    public double calculateRMS(double[] samples) {
        return Arrays.stream(samples).map(x -> x * x).sum();
    }

    public double[] removeAudioLowerByRMS(double[] samples, double rms, double threshold) {
        if (rms > threshold) {
            Arrays.fill(samples, 0.0);
        }
        return samples;
    }

    public double calculateAmplitudeThreshold(double totalSumOfSquares, int totalSamples) {
        return thresholdMultiplier * Math.sqrt(totalSumOfSquares / totalSamples);
    }
}
