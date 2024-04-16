package GameManagers.Monsters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import AppContext.MyApplication;
import Database.DataAccess.MonstersDataAccess;
import Database.DataUpdates.MonstersDataUpdate;
import Database.DatabaseConnection;
import Dates.DateManager;
import GameManagers.Challenges.ChallengesUpdater;
import GameManagers.ExperienceManager;
import GameManagers.Missions.MissionsUpdater;
import GameManagers.Monsters.AppearingConditions;
import GameManagers.Monsters.DisappearanceConditions;

public class MonstersManager {
    private final ChallengesUpdater challengesUpdater;
    private final MonstersDataAccess monstersDataAccess;
    private final MonstersDataUpdate monstersDataUpdate;
    private final MissionsUpdater missionsUpdater;
    private final DateManager dateManager;
    private final ExperienceManager experienceManager;

    public MonstersManager() {
        DatabaseConnection connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
        challengesUpdater = new ChallengesUpdater(connection);
        monstersDataAccess = new MonstersDataAccess(connection);
        monstersDataUpdate = new MonstersDataUpdate(connection);

        experienceManager = new ExperienceManager();
        missionsUpdater = new MissionsUpdater();
        dateManager = new DateManager();
    }

    public void updateMonster(boolean[] appearingMonsters, boolean[] defeatedMonsters) {
        for (boolean monster : appearingMonsters) {
            System.out.print("Monstruo: " + monster + " ");
        }

        if (monstersDataAccess.getActiveMonster() == -1) { // Si no hay monstruo activo //
            if (isMonsterAppearing(appearingMonsters) && new Random().nextBoolean()) { // 50% de probabilidad de que aparezca
                int selectedMonster = selectMonster(appearingMonsters);
                System.out.println("Monstruo seleccionado: " + selectedMonster);
                monstersDataUpdate.updateMonsterActiveStatus(selectedMonster, dateManager.getCurrentDate());
                challengesUpdater.updateMonsterAppearedRecord(true);
                System.out.println("Apareció un monstruo");
            } else {
                challengesUpdater.updateMonsterAppearedRecord(false);
                missionsUpdater.updateMission5(); // Apareció un monstruo pero no se activó
                System.out.println("No apareció un monstruo");
            }
        } else { // Si hay monstruo activo
            if (dateManager.haveThreeDaysPassed(monstersDataAccess.getDateAppearedActiveMonster())) {
                monstersDataUpdate.updateMonsterInactiveStatus(monstersDataAccess.getActiveMonster());
            } else { // Si no han pasado 3 días
                handleActiveMonster(defeatedMonsters);
            }
        }
    }

    private int selectMonster(boolean[] appearingMonsters) {
        int selectedMonster;
        do {
            selectedMonster = new Random().nextInt(appearingMonsters.length);
        } while (!appearingMonsters[selectedMonster]);
        return selectedMonster + 1; // Se suma 1 porque los monstruos están indexados desde 1
    }

    private boolean isMonsterAppearing(boolean[] monsters) {
        for (boolean monster : monsters) {
            if (monster) return true;
        }
        return false;
    }

    private void handleActiveMonster(boolean[] defeatedMonsters) {
        if (defeatedMonsters[monstersDataAccess.getActiveMonster()]) {
            monstersDataUpdate.updateMonsterInactiveStatus(monstersDataAccess.getActiveMonster());
            missionsUpdater.updateMission18();
            experienceManager.addExperience(500);
        }
    }
}