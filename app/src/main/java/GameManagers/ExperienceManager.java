package GameManagers;

import AppContext.MyApplication;
import Database.DataAccess.AvatarDataAccess;
import Database.DataUpdates.AvatarDataUpdate;
import Database.DatabaseConnection;
import GameManagers.Challenges.ChallengesUpdater;
import GameManagers.Missions.MissionsUpdater;
import Rewards.Rewards;

public class ExperienceManager {
    private final DatabaseConnection connection;
    private final AvatarDataAccess avatarDataAccess;
    private final AvatarDataUpdate avatarDataUpdate;

    public ExperienceManager() {
        connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
        connection.openDatabase();

        avatarDataAccess = new AvatarDataAccess(connection);
        avatarDataUpdate = new AvatarDataUpdate(connection);
    }

    public static int calculateRequiredExperience(byte level) {
        return 100 * (level + 1) * (level + 2);
    }

    public void addExperience(int experience) {
        experience += avatarDataAccess.getCurrentExperience();
        avatarDataUpdate.updateExperience(experience);
        if (itCanLevelUp(experience)) {
            levelUp();
        }
        System.out.println("Se ha ganado " + experience + " de experiencia");

        if (experience >= 100) {
            ChallengesUpdater challengesUpdater = new ChallengesUpdater(connection);
            challengesUpdater.updateExperienceRecord();
        }
    }

    private void levelUp() {
        int currentLevel = avatarDataAccess.getLevel();
        int newLevel = currentLevel + 1;

        avatarDataUpdate.updateLevel(newLevel);
        avatarDataUpdate.updateRequiredExperience(calculateRequiredExperience(avatarDataAccess.getLevel()));
        MissionsUpdater missionsUpdater = new MissionsUpdater();
        missionsUpdater.updateMission7();
        Rewards.giveReward();

        System.out.println("Se ha subido de nivel: " + newLevel);
    }

    private boolean itCanLevelUp(int experience) {
        return experience >= avatarDataAccess.getRequiredExperience() && avatarDataAccess.getLevel() < 50;
    }
}