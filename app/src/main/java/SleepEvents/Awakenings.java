package SleepEvents;

public class Awakenings {
    public boolean isAwakening(double bpm, int currentPhase) {
        return bmpIsHigh(bpm) && isValidPhase(currentPhase);
    }

    private boolean bmpIsHigh(double bpm) {
        return bpm > 65;
    }
    private boolean isValidPhase(int currentPhase) {
        return currentPhase == 1; // Sueno ligero
    }
}
