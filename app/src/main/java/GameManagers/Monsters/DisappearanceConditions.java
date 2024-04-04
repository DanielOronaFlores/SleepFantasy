package GameManagers.Monsters;

public class DisappearanceConditions {
    private int efficiency;
    private int time;
    private int lpm;
    private int movements;
    private int positionChanges;
    private boolean vertical;

    public void setEfficiency(int efficiency) {
        this.efficiency = efficiency;
    }
    public void setTime(int time) {
        this.time = time;
    }
    public void setLpm(int lpm) {
        this.lpm = lpm;
    }
    public void setMovements(int movements) {
        this.movements = movements;
    }
    public void setPositionChanges(int positionChanges) {
        this.positionChanges = positionChanges;
    }
    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    public boolean isDefeatedMonster(int monster) {
        switch (monster) {
            case 1:
                return wonInsomnia(efficiency);
            case 2:
                return wonLoudSound(time);
            case 3:
                return wonAnxiety(lpm, positionChanges, movements);
            case 4:
                return wonNightmare(movements, lpm);
            case 5:
                return wonSomnambulism(vertical);
            default:
                return false;
        }
    }

    private static boolean wonInsomnia(int efficiency) {
        return efficiency >= 80;
    }
    private static boolean wonLoudSound(int time) {
        return time <= 30; // Las cantidades se guardan en minutos
    }
    private static boolean wonAnxiety(int lpm, int positionChanges, int suddenMovements) { // Cada hora
        return lpm < 80 && suddenMovements < 20 && positionChanges < 50;
    }
    private static boolean wonNightmare(int lpm, int movements) { // Cada hora
        return lpm < 80 && movements < 30;
    }
    private static boolean wonSomnambulism(boolean vertical) { // Cada hora
        return vertical;
    }
}