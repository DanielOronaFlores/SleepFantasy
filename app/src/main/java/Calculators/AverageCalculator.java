package Calculators;

import java.util.List;

public class AverageCalculator {
    public double calculateMeanDouble(List<Double> values) {
        double suma = 0.0;
        for (Double value : values) {
            suma += value;
        }
        return suma / values.size();
    }

    public float calculateMeanFloat(List<Float> values) {
        float suma = 0;
        for (Float value : values) {
            suma += value;
        }
        return suma / values.size();
    }
}
