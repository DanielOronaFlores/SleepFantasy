package Calculators;

public class SleepData {
    public static int getTimeInBed(int vigilTime, int totalSleepTime){
        return vigilTime + totalSleepTime;
    }

    public static int getSleepEfficiency(int timeSleep, int timeInBed) {
        if (timeInBed == 0) return 0;
        return (timeSleep / timeInBed) * 100;
    }

    public static int getTotalSleepTime(int lightSleepTime, int deepSleepTime, int remSleepTime) {
        return lightSleepTime + deepSleepTime + remSleepTime;
    }
}
