package Calculators;

public class Efficiency {
    public static int getSleepEfficiency(int totalSleep, int vigil) {
        if (vigil == 0) {
            return 0;
        }
        int PERCENTAGE = 100;
        return (totalSleep / vigil) * PERCENTAGE;
    }
}
