package Calculators;

public class Efficiency {
    public static int getSleepEfficiency(int totalSleep, int vigil) {
        int PERCENTAGE = 100;
        return (totalSleep / vigil) * PERCENTAGE;
    }
}
