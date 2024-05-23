package GameManagers.Monsters;

public class MonsterConditions {
    public static boolean isInsomnia(int efficiency) {
        return efficiency < 80;
    }
    public static boolean isLoudSound(int time) {
        return time > 1800;
    }
    public static boolean isAnxiety(int lpm, int positionChanges, int movements) { // Cada hora
        return lpm > 80 && movements > 10 && positionChanges > 5;
    }
    public static boolean isNightmare(int lpm, int movements) { // Cada hora
        return lpm > 80 && movements > 30;
    }
    public static boolean isSomnambulism(int sleepPhase) {
        return sleepPhase == 2;
    }
}