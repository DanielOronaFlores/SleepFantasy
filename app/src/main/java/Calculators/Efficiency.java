package Calculators;

public class Efficiency {
    public static int getSleepEfficiency(int timeSleep, int timeInBed) {
        if (timeInBed == 0) {
            return 0;
        }
        return (timeSleep / timeInBed) * 100;
    }
}
