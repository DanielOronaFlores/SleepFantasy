package GameManagers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Random;

import AppContext.MyApplication;
import Database.DataAccess.MonstersDataAccess;
import Database.DataUpdates.MonstersDataUpdate;
import Database.DataUpdates.RecordsDataUpdate;
import Database.DatabaseConnection;
import Dates.DateManager;
import GameManagers.Challenges.ChallengesUpdater;

public class MonstersManager {
    private final DatabaseConnection connection;
    private final MonstersDataAccess monstersDataAccess;
    private final MonstersDataUpdate monstersDataUpdate;
    private final RecordsDataUpdate recordsDataUpdate;
    private final DateManager dateManager = new DateManager();
    private final ExperienceManager experienceManager = new ExperienceManager();

    public MonstersManager() {
        connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
        monstersDataAccess = new MonstersDataAccess(connection);
        recordsDataUpdate = new RecordsDataUpdate(connection);
        monstersDataUpdate = new MonstersDataUpdate(connection);
    }

    public void updateMonster() throws ParseException {
        if (monstersDataAccess.getActiveMonster() == -1)
        {
            int efficiency = 60; //TODO: Obtener eficiencia
            int time = 50; //TODO: Obtener tiempo
            int lpm = 50; //TODO: Obtener lpm
            int sdnn = 30; //TODO: Obtener sdnn
            int movements = 35; //TODO: Obtener movimientos
            int selectedMonster = 0;

            ArrayList<Integer> monsters = selectMonster(efficiency, time, lpm, sdnn, movements);
            if (!monsters.isEmpty()) {
                if (monsters.size() >= 1) {
                    int index = selectRandomMonster(monsters.size() - 1);
                    selectedMonster = monsters.get(index); //TODO: Se debe alamcenar el mosntruo seleccionado en algun lado
                }

                ChallengesUpdater challengesUploader = new ChallengesUpdater(connection);
                if (probability()) {
                    monstersDataUpdate.updateMonsterActiveStatus(selectedMonster, dateManager.getCurrentDate());
                    challengesUploader.updateMonsterAppearedRecord(true);
                } else {
                    challengesUploader.updateMonsterAppearedRecord(false);
                }
            }
        } else {
            if (dateManager.haveThreeDaysPassed(monstersDataAccess.getDateAppearedActiveMonster())) {
               monstersDataUpdate.updateMonsterInactiveStatus(monstersDataAccess.getActiveMonster());
            } else {
                if (isDefeatedMonster(monstersDataAccess.getActiveMonster())) {
                    monstersDataUpdate.updateMonsterInactiveStatus(monstersDataAccess.getActiveMonster());
                    experienceManager.addExperience(500);
                } else {
                    monstersDataUpdate.updateMonsterOldDate(monstersDataAccess.getActiveMonster(), dateManager.getCurrentDate());
                }
            }
        }
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

    //CONDICIONES DE DESAPARICIÓN DE MONSTRUOS
    private boolean isDefeatedMonster(int monster) {
        int efficiency = 60; //TODO: Obtener eficiencia
        int time = 50; //TODO: Obtener tiempo
        int lpm = 50; //TODO: Obtener lpm
        int sdnn = 30; //TODO: Obtener sdnn
        int movements = 35; //TODO: Obtener movimientos

        switch (monster) {
            case 1:
                return wonInsomnia(efficiency);
            case 2:
                return wonLoudSound(time);
            case 3:
                return wonAnxiety(lpm, sdnn, movements);
            case 4:
                return wonNightmare(movements, lpm);
            case 5:
                return wonSomnambulism(movements);
            default:
                return false;
        }
    }

    private boolean wonInsomnia(int efficiency) {
        return efficiency >= 80;
    }
    private boolean wonLoudSound(int time) {
        return time <= 30; // Las cantidades se guardan en minutos
    }
    private boolean wonAnxiety(int lpm, int sdnn, int movements) { // Cada hora
        return lpm < 80 && sdnn > 50 && movements < 20;
    }
    private boolean wonNightmare(int movements, int lpm) { // Cada hora
        return movements < 20 && lpm < 80;
    }
    private boolean wonSomnambulism(int movements) { // Cada hora
        boolean vertical = false; // TODO: Debe comprobar el valor del acelerómetro.
        return vertical;
    }

    //OTROS METODOS PARA LA CLASE
    private static boolean probability() {
        return new Random().nextBoolean();
    }
    private static int selectRandomMonster(int size) {
        return new Random().nextInt(size);
    }
}
