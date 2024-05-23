package SleepEvents;

public class Awakenings {
    public boolean isAwakening(boolean isVertical, int currentPhase) {
        return isVertical && currentPhase > 0;
    }
}
