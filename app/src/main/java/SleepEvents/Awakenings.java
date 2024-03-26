package SleepEvents;

public class Awakenings {
    public boolean isAwakening(double bpm, int currentPhase) {
        return bmpIsHigh(bpm) && isValidPhase(currentPhase);
    }

    private boolean bmpIsHigh(double bpm) {
        return bpm > 60;
    }
    private boolean isValidPhase(int currentPhase) {
        return currentPhase >= 1 && currentPhase <= 2;
    }
}
