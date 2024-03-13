package SleepTracker;

public class SleepCycleCalculator {
    public String getSleepCycle(double bpmMean){
        String sleepCycle;
        if (bpmMean > 60 && bpmMean < 100) {
            sleepCycle = "vigilia";
        } else if (bpmMean > 40 && bpmMean < 80) {
            sleepCycle = "sueño ligero";
        } else if (bpmMean > 20 && bpmMean < 60) {
            sleepCycle = "sueño profundo";
        } else {
            sleepCycle = "noes";
        }
        return sleepCycle;
    }
}
