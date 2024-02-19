package SoundEcofilter;

import java.util.Arrays;

public class RootMeanSquare {
    private final double thresholdMultiplier;

    public RootMeanSquare(double thresholdMultiplier) {
        this.thresholdMultiplier = thresholdMultiplier;
    }

    public double calculateRMS(double[] samples) {
        return Arrays.stream(samples).map(x -> x * x).sum();
    }

    public boolean removeAudioLowerByRMS(double[] samples, double rms, double threshold) {
        return rms > threshold;
    }

    public double calculateAmplitudeThreshold(double totalSumOfSquares, int totalSamples) {
        return thresholdMultiplier * Math.sqrt(totalSumOfSquares / totalSamples);
    }
}