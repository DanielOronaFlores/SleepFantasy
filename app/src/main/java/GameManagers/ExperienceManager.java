package GameManagers;

import AppContext.MyApplication;
import Database.DataAccess.AvatarDataAccess;
import Database.DataUpdates.AvatarDataUpdate;
import Database.DatabaseConnection;
import GameManagers.Challenges.ChallengesUpdater;

public class ExperienceManager {
    private DatabaseConnection connection;
    private final AvatarDataAccess avatarDataAccess;
    private final AvatarDataUpdate avatarDataUpdate;

    public ExperienceManager() {
        connection = DatabaseConnection.getInstance(MyApplication.getAppContext());

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

    public void levelUp() {
        avatarDataUpdate.updateLevel(avatarDataAccess.getLevel() + 1);
        avatarDataUpdate.updateRequiredExperience(calculateRequiredExperience(avatarDataAccess.getLevel()));
    }

    public boolean itCanLevelUp(int experience) {
        return experience >= avatarDataAccess.getRequiredExperience() && avatarDataAccess.getLevel() < 50;
    }
}