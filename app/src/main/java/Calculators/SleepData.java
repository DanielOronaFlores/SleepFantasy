package Calculators;

public class SleepData {
    public static int getTimeInBed(int vigilTime, int totalSleepTime){
        return vigilTime + totalSleepTime;
    }

    public static int getSleepEfficiency(float timeSleep, float timeInBed) {
        System.out.println("Eficienica de sueño");
        System.out.println("timeSleep: " + timeSleep);
        System.out.println("timeInBed: " + timeInBed);
        if (timeInBed == 0) return 0;
        return (int) ((timeSleep / timeInBed) * 100);
    }

    public static int getTotalSleepTime(int lightSleepTime, int deepSleepTime, int remSleepTime) {
        return lightSleepTime + deepSleepTime + remSleepTime;
    }
}
