package GameManagers.Monsters;

public class DisappearanceConditions {
    public static boolean wonInsomnia(int efficiency) {
        return efficiency >= 80;
    }
    public static boolean wonLoudSound(int time) {
        return time <= 30; // Las cantidades se guardan en minutos
    }
    public static boolean wonAnxiety(int lpm, int positionChanges, int suddenMovements) { // Cada hora
        return lpm < 80 && suddenMovements < 20 && positionChanges < 50;
    }
    public static boolean wonNightmare(int lpm, int movements) { // Cada hora
        return lpm < 80 && movements < 30;
    }
    public static boolean wonSomnambulism(boolean vertical) { // Cada hora
        return vertical;
    }
}