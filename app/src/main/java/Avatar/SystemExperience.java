package Avatar;

import Database.Avatar.AvatarDataAccess;
import Database.Avatar.AvatarDataUpdate;
import Database.DatabaseConnection;

public class SystemExperience {
    private AvatarDataAccess avatarDataAccess;
    private AvatarDataUpdate avatarDataUpdate;
    private DatabaseConnection connection;

    public SystemExperience(DatabaseConnection connection) {
        this.connection = connection;
        this.avatarDataAccess = new AvatarDataAccess(connection);
        this.avatarDataUpdate = new AvatarDataUpdate(connection);
    }

    public static int calculateRequiredExperience(byte level) {
        int requiredExperience = 100 * (level + 1) * (level + 2);
        return requiredExperience;
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