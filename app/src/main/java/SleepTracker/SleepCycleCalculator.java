package SleepTracker;

public class SleepCycleCalculator {
    public int getSleepCycle(double bpmMean){
        if (bpmMean > 60 && bpmMean < 100) {
            return 2;
        } else if (bpmMean > 50 && bpmMean < 60) {
            return 3;
        } else if (bpmMean > 40 && bpmMean < 50) {
            return 4;
        }
        return 0;
    }

    public boolean hasAwakeningEnded(double bpmMean){
        return bpmMean < 60;
    }
    public boolean hasLightEnded(double bpmMean){
        return bpmMean < 55;
    }
    public boolean hasDeepEnded(double bpmMean){
        return bpmMean > 60;
    }
}
