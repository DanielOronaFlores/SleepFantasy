package SleepTracker;

import java.util.List;

public class SDNNCalculator {
    public double calculateSDNN(List<Long> rrIntervals) {
        long sum = 0;
        for (Long rrInterval : rrIntervals) {
            sum += rrInterval;
        }
        double mean = (double) sum / rrIntervals.size();

        // Calcular la desviación estándar
        double sd = 0;
        for (Long rrInterval : rrIntervals) {
            sd += Math.pow(rrInterval - mean, 2);
        }
        return Math.sqrt(sd / rrIntervals.size());
    }
}