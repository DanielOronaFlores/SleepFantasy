package SleepTracker;

import java.util.List;

public class HRVCalculator {
    public double calculateSDNN(List<Double> rrIntervals, double rrSummation) {
        double n = rrIntervals.size();
        return Math.sqrt(rrSummation / (n - 1));
    }

    public double calculateRRSummation(List<Double> rrIntervals, double rrMean) {
        double summation = 0;
        for (int i = 0; i < rrIntervals.size(); i++) {
            double difference = rrIntervals.get(i) - rrMean;
            summation += Math.pow(difference, 2);
        }
        return summation;
    }

    public double calculateRMSSD(List<Double> rrIntervals) {
        int n = rrIntervals.size();
        double sumOfSquaredDifferences = 0.0;

        for (int i = 0; i < n - 1; i++) {
            double difference = rrIntervals.get(i + 1) - rrIntervals.get(i);
            sumOfSquaredDifferences += difference * difference;
        }

        double meanSquaredDifference = sumOfSquaredDifferences / (n - 1);
        return Math.sqrt(meanSquaredDifference);
    }

    public double calculateHRV(double sdnn, double rmssd) {
        return sdnn / rmssd;
    }
}