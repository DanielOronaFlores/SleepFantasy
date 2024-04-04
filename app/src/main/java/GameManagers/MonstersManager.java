package GameManagers;

import java.util.ArrayList;
import java.util.Random;

import AppContext.MyApplication;
import Database.DataAccess.MonstersDataAccess;
import Database.DataUpdates.MonstersDataUpdate;
import Database.DatabaseConnection;
import Dates.DateManager;
import GameManagers.Challenges.ChallengesUpdater;
import GameManagers.Missions.MissionsUpdater;
import GameManagers.Monsters.AppearingConditions;
import GameManagers.Monsters.DisappearanceConditions;

public class MonstersManager {
    private final DatabaseConnection connection;
    private final MonstersDataAccess monstersDataAccess;
    private final MonstersDataUpdate monstersDataUpdate;
    private final MissionsUpdater missionsUpdater;
    private final DateManager dateManager;
    private final ExperienceManager experienceManager;
    private final int efficiency, soundsTime, lpm, positionChanges, suddenMovements;
    private final boolean vertical;

    public MonstersManager(int efficiency, int soundsTime, int lpm, int positionChanges, int suddenMovements, boolean vertical) {
        this.connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
        this.monstersDataAccess = new MonstersDataAccess(connection);
        this.missionsUpdater = new MissionsUpdater();
        this.monstersDataUpdate = new MonstersDataUpdate(connection);
        this.dateManager = new DateManager();
        this.experienceManager = new ExperienceManager();

        this.efficiency = efficiency;
        this.soundsTime = soundsTime;
        this.lpm = lpm;
        this.positionChanges = positionChanges;
        this.suddenMovements = suddenMovements;
        this.vertical = vertical;
    }

    public void updateMonster() {
        connection.openDatabase();
        if (monstersDataAccess.getActiveMonster() == -1) {
            ArrayList<Integer> monsters = selectMonster();
            if (!monsters.isEmpty()) {
                int index = selectRandomMonster(monsters.size());
                int selectedMonster = monsters.get(index);

                ChallengesUpdater challengesUploader = new ChallengesUpdater(connection);

                if (probability()) {
                    monstersDataUpdate.updateMonsterActiveStatus(selectedMonster, dateManager.getCurrentDate());
                    challengesUploader.updateMonsterAppearedRecord(true);
                } else {
                    challengesUploader.updateMonsterAppearedRecord(false);
                    missionsUpdater.updateMission5();
                }
            }
        } else {
            if (dateManager.haveThreeDaysPassed(monstersDataAccess.getDateAppearedActiveMonster())) {
                monstersDataUpdate.updateMonsterInactiveStatus(monstersDataAccess.getActiveMonster());
            } else {
                handleActiveMonster();
            }
        }
        connection.closeDatabase();
    }

    private ArrayList<Integer> selectMonster() {
        AppearingConditions appearingConditions = new AppearingConditions();
        ArrayList<Integer> monsters = new ArrayList<>();

        if (appearingConditions.isInsomnia(efficiency))
            monsters.add(1); // Insomnio
        if (appearingConditions.isLoudSound(soundsTime))
            monsters.add(2); // Sonido fuerte
        if (appearingConditions.isAnxiety(lpm, positionChanges, suddenMovements))
            monsters.add(3); // Ansiedad
        if (appearingConditions.isNightmare(lpm, suddenMovements))
            monsters.add(4); // Pesadilla
        if (appearingConditions.isSomnambulism(suddenMovements, vertical))
            monsters.add(5); // Sonambulismo

        return monsters;
    }

    private void handleActiveMonster() {
        boolean isMonsterDefeated = isMonsterDefeated();

        if (isMonsterDefeated) {
            monstersDataUpdate.updateMonsterInactiveStatus(monstersDataAccess.getActiveMonster());
            missionsUpdater.updateMission18();
            experienceManager.addExperience(500);
        } else {
            monstersDataUpdate.updateMonsterOldDate(monstersDataAccess.getActiveMonster(), dateManager.getCurrentDate());
        }
    }

    private boolean isMonsterDefeated() {
        DisappearanceConditions disappearanceConditions = new DisappearanceConditions();
        disappearanceConditions.setEfficiency(efficiency);
        disappearanceConditions.setTime(soundsTime);
        disappearanceConditions.setLpm(lpm);
        disappearanceConditions.setMovements(suddenMovements);
        disappearanceConditions.setPositionChanges(positionChanges);
        disappearanceConditions.setVertical(vertical);
        return disappearanceConditions.isDefeatedMonster(monstersDataAccess.getActiveMonster());
    }

    private static boolean probability() {
        return new Random().nextBoolean();
    }
    private static int selectRandomMonster(int size) {
        return new Random().nextInt(size);
    }
}
