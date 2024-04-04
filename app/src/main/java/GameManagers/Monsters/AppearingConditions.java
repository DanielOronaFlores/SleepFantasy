package GameManagers.Monsters;

public class AppearingConditions {
    public boolean isInsomnia(int efficiency) {
        return efficiency < 80;
    }
    public boolean isLoudSound(int time) {
        return time > 30;
    }
    public boolean isAnxiety(int lpm, int positionChanges, int suddenMovements) { // Cada hora
        return lpm > 80 && suddenMovements > 20 && positionChanges > 50;
    }
    public boolean isNightmare(int lpm, int movements) { // Cada hora
        return lpm > 80 && movements > 30;
    }
    public boolean isSomnambulism(int movements, boolean vertical) {
        return movements > 30 && vertical;
    }
}