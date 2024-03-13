package SleepTracker;

import java.util.List;

public class SDNNCalculator {
    public static void main(String[] args) {


    }


    public double calculateMean(List<Double> values) {
        double suma = 0.0;
        for (Double value : values) {
            suma += value;
        }
        return suma / values.size();
    }
}