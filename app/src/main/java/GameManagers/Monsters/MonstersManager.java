package GameManagers.Monsters;

import java.util.Random;

import AppContext.MyApplication;
import Database.DataAccess.MonstersDataAccess;
import Database.DataUpdates.MonstersDataUpdate;
import Database.DatabaseConnection;
import Dates.DateManager;
import GameManagers.Challenges.ChallengesUpdater;
import GameManagers.ExperienceManager;
import GameManagers.Missions.MissionsUpdater;

public class MonstersManager {
    private final ChallengesUpdater challengesUpdater;
    private final MonstersDataAccess monstersDataAccess;
    private final MonstersDataUpdate monstersDataUpdate;
    private final MissionsUpdater missionsUpdater;
    private final ExperienceManager experienceManager;

    public MonstersManager() {
        DatabaseConnection connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
        challengesUpdater = new ChallengesUpdater();
        monstersDataAccess = new MonstersDataAccess(connection);
        monstersDataUpdate = new MonstersDataUpdate(connection);

        experienceManager = new ExperienceManager();
        missionsUpdater = new MissionsUpdater();
    }

    public void updateMonster(boolean[] monsterConditions) { // appearingMonsters = {insomnia, loudSound, anxiety, nightmare, somnambulism}
        if (monstersDataAccess.getActiveMonster() == -1) { // Si no hay monstruo activo //
            if (isMonsterAppearing(monsterConditions) && new Random().nextBoolean()) { // 50% de probabilidad de que aparezca
                int selectedMonster = selectMonster(monsterConditions);
                monstersDataUpdate.updateMonsterActiveStatus(selectedMonster, DateManager.getCurrentDate());
                challengesUpdater.updateMonsterAppearedRecord(true);
            } else {
                challengesUpdater.updateMonsterAppearedRecord(false);
                missionsUpdater.updateMission5(); // Apareció un monstruo pero no se activó
            }
        } else { // Si hay monstruo activo
            String dateAppeared = monstersDataAccess.getDateAppearedActiveMonster();
            String currentDate = DateManager.getCurrentDate();
            if (DateManager.getDaysDifference(dateAppeared, currentDate) >= 3) {
                monstersDataUpdate.updateMonsterInactiveStatus(monstersDataAccess.getActiveMonster());
            } else { // Si no han pasado 3 días
                handleActiveMonster(monsterConditions);
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

    private void handleActiveMonster(boolean[] monsterConditions) {
        if (!monsterConditions[monstersDataAccess.getActiveMonster() - 1]) { // False significa que el monstruo ha sido derrotado
            monstersDataUpdate.updateMonsterInactiveStatus(monstersDataAccess.getActiveMonster());
            missionsUpdater.updateMission18();
            experienceManager.addExperience(500);
        }
    }
}