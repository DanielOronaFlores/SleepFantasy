package GameManagers;

public class MonstersManager {

    private boolean isInsomnia(int efficiency) {
        return efficiency > 80;
    }

    private boolean isLoudSound(int time) {
        return time > 30; //Las cantidades se guardan en minutos
    }

    private boolean isAnxiety(int lpm, int sdnn, int movements) { //Cada hora
        return lpm > 80 && sdnn < 50 && movements > 20;
    }

    private boolean isNightmare(int movements, int lpm) { //Cada hora
        return movements > 30 && lpm > 80;
    }

    private boolean isSomnambulism(int movements) { //Cada hora
        boolean vertical = true; //TODO: Debe comprobar el valor del acelerometro.
        return movements > 30 && vertical;
    }
}
