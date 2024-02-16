package SleepTracker;

public class Efficiency {
    public int getSleepEfficiency(int totalSleep, int vigil) {
        int PERCENTAGE = 100;
        return (totalSleep / vigil) * PERCENTAGE;
    }
}
