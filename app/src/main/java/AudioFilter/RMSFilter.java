package AudioFilter;

import java.util.Arrays;

public class RMSFilter {
    private final int thresholdMultiplier;
    public RMSFilter(int thresholdMultiplier) {
        this.thresholdMultiplier = thresholdMultiplier;
    }

    public void removeAudioLowerByRMS(double[] samples, double rms, double threshold) {
        if (rms > threshold) {
            Arrays.fill(samples, 0.0);
        }
    }

    public double calculateAmplitudeThreshold(double totalSumOfSquares, int totalSamples) {
        return thresholdMultiplier * Math.sqrt(totalSumOfSquares / totalSamples);
    }
}
