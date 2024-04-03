package GameManagers;

import AppContext.MyApplication;
import Database.DataAccess.AvatarDataAccess;
import Database.DataUpdates.AvatarDataUpdate;
import Database.DatabaseConnection;
import GameManagers.Challenges.ChallengesUpdater;
import GameManagers.Missions.MissionsUpdater;

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

        if (experience >= 500) {
            ChallengesUpdater challengesUpdater = new ChallengesUpdater(connection);
            challengesUpdater.updateExperienceRecord();
        }
    }

    private void levelUp() {
        avatarDataUpdate.updateLevel(avatarDataAccess.getLevel() + 1);
        avatarDataUpdate.updateRequiredExperience(calculateRequiredExperience(avatarDataAccess.getLevel()));
        MissionsUpdater missionsUpdater = new MissionsUpdater();
        missionsUpdater.updateMission7();
    }

    private boolean itCanLevelUp(int experience) {
        return experience >= avatarDataAccess.getRequiredExperience() && avatarDataAccess.getLevel() < 50;
    }
}