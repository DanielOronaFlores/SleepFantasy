package Utils;

import java.util.List;

public class AverageCalculator {
    public double calculateMean(List<Double> values) {
        double suma = 0.0;
        for (Double value : values) {
            suma += value;
        }
        return suma / values.size();
    }
}
