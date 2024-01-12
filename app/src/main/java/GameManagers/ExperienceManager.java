package GameManagers;

import AppContext.MyApplication;
import DataAccess.AvatarDataAccess;
import DataUpdates.AvatarDataUpdate;
import Database.DatabaseConnection;

public class ExperienceManager {
    private final AvatarDataAccess avatarDataAccess;
    private final AvatarDataUpdate avatarDataUpdate;

    public ExperienceManager() {
        DatabaseConnection connection = DatabaseConnection.getInstance(MyApplication.getAppContext());

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
    }

    public void levelUp() {
        avatarDataUpdate.updateLevel(avatarDataAccess.getLevel() + 1);
        avatarDataUpdate.updateRequiredExperience(calculateRequiredExperience(avatarDataAccess.getLevel()));
    }

    public boolean itCanLevelUp(int experience) {
        return experience >= avatarDataAccess.getRequiredExperience() && avatarDataAccess.getLevel() < 50;
    }
}