package SleepEvaluator;

public class PercentageConverter {
    public static float convertToPercentage(float value, float total) {
        System.out.println("Value: " + value + " Total: " + total);
        System.out.println("Percentage: " + (value / total) * 100);
        return (value / total) * 100;
    }
}
