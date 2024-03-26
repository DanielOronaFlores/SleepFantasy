package Calculators;

public class SleepCycle {
    public boolean hasAwakeningEnded(double bpmMean){
        return bpmMean < 60;
    }
    public boolean hasLightEnded(double bpmMean){
        return bpmMean <= 50;
    }
    public boolean hasDeepEnded(double bpmMean) {
        return bpmMean >= 50;
    }
    public boolean hasREMEnded(double bpmMean){
        return bpmMean < 60;
    }
}
