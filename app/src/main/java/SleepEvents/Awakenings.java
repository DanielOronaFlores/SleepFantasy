package SleepEvents;

public class Awakenings {
    public boolean isAwakening(boolean isVertical, int currentPhase) {
        return isVertical && isValidPhase(currentPhase);
    }

    private boolean isValidPhase(int currentPhase) {
        return currentPhase > 0; // Mayor a Vigilia
    }
}
