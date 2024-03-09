package Calculators;

public class Efficiency {
    public int getSleepEfficiency(int totalSleep, int vigil) { //TODO: Implementar este metodo al terminar el ciclo de sueño
        int PERCENTAGE = 100;
        return (totalSleep / vigil) * PERCENTAGE;
    }
}
