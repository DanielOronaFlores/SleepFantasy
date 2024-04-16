package GameManagers.Monsters;

public class AppearingConditions {
    public static boolean isInsomnia(int efficiency) {
        return efficiency < 80;
    }
    public static boolean isLoudSound(int time) {
        return time > 30;
    }
    public static boolean isAnxiety(int lpm, int positionChanges, int movements) { // Cada hora
        return lpm > 80 && movements > 20 && positionChanges > 50;
    }
    public static boolean isNightmare(int lpm, int movements) { // Cada hora
        return lpm > 80 && movements > 30;
    }
    public static boolean isSomnambulism(int movements, boolean vertical) {
        return movements > 30 && vertical;
    }
}