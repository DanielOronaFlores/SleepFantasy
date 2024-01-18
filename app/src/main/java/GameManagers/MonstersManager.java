package GameManagers;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import AppContext.MyApplication;
import DataAccess.RecordsDataAccess;
import DataUpdates.RecordsDataUpdate;
import Database.DatabaseConnection;

public class MonstersManager {
    private RecordsDataAccess recordsDataAccess;
    private RecordsDataUpdate recordsDataUpdate;

    public MonstersManager() {
        DatabaseConnection connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
        recordsDataAccess = new RecordsDataAccess(connection);
        recordsDataUpdate = new RecordsDataUpdate(connection);
    }

    public void updateMonster() {
        if (!recordsDataAccess.hasMonsterAppeared())
        {
            int efficiency = 60; //TODO: Obtener eficiencia
            int time = 50; //TODO: Obtener tiempo
            int lpm = 50; //TODO: Obtener lpm
            int sdnn = 30; //TODO: Obtener sdnn
            int movements = 35; //TODO: Obtener movimientos
            int selectedMonster = 0;

            ArrayList<Integer> monsters = selectMonster(efficiency, time, lpm, sdnn, movements);
            if (!monsters.isEmpty()) {
                if (monsters.size() > 1) {
                    int index = selectRandomMonster(monsters.size() - 1);
                    selectedMonster = monsters.get(index); //TODO: Se debe alamcenar el mosntruo seleccionado en algun lado
                }
                if (probability()) {
                    recordsDataUpdate.updateMonsterAppeared();
                }
            } else {
               Log.d("MonstersManager", "No hay monstruos disponibles");
            }
        }
        Log.d("MonstersManager", "El monstruo esta activo");
    }

    private ArrayList<Integer> selectMonster(int efficiency, int time, int lpm, int sdnn, int movements) {
        ArrayList<Integer> monsters = new ArrayList<>();

        if (isInsomnia(efficiency)) monsters.add(1); // Insomnio
        if (isLoudSound(time)) monsters.add(2); // Sonido fuerte
        if (isAnxiety(lpm, sdnn, movements)) monsters.add(3); // Ansiedad
        if (isNightmare(movements, lpm)) monsters.add(4); // Pesadilla
        if (isSomnambulism(movements)) monsters.add(5); // Sonambulismo

        return monsters;
    }

    //CONDICIONES DE APARICIÓN DE MONSTRUOS
    private boolean isInsomnia(int efficiency) {
        return efficiency < 80;
    }

    private boolean isLoudSound(int time) {
        return time > 30; // Las cantidades se guardan en minutos
    }

    private boolean isAnxiety(int lpm, int sdnn, int movements) { // Cada hora
        return lpm > 80 && sdnn < 50 && movements > 20;
    }

    private boolean isNightmare(int movements, int lpm) { // Cada hora
        return movements > 30 && lpm > 80;
    }

    private boolean isSomnambulism(int movements) { // Cada hora
        boolean vertical = true; // TODO: Debe comprobar el valor del acelerómetro.
        return movements > 30 && vertical;
    }

    //OTROS METODOS PARA LA CLASE
    private static boolean probability() {
        return new Random().nextBoolean();
    }

    private static int selectRandomMonster(int size) {
        return new Random().nextInt(size);
    }
}
