package Rewards;

import java.util.List;

import AppContext.MyApplication;
import Database.DataAccess.RewardsDataAccess;
import Database.DataUpdates.RewardsDataUpdate;
import Database.DatabaseConnection;

public class Rewards {
    private static RewardsDataAccess rewardsDataAccess;
    private static DatabaseConnection connection;

    private static void initializeInstances() {
        connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
        rewardsDataAccess = new RewardsDataAccess(connection);
    }

    private static int selectRandomReward() {
        List<Integer> rewards = rewardsDataAccess.getAllUngivenRewards();
        int randomIndex = (int) (Math.random() * rewards.size());
        return rewards.get(randomIndex);
    }

    public static void giveReward() {
        initializeInstances();
        int reward = selectRandomReward();
        int rewardType = rewardsDataAccess.getRewardType(reward);
        connection.openDatabase();

        switch (rewardType) {
            case 1:
                AudiosRewards.giveRewardAudio(reward, connection);
                break;
            case 2: // Temas de intefaz

                break;
            case 3: // Sonidos Notificacion
                int totalRewards = rewardsDataAccess.getTotalRewards();
                SoundsRewards.giveRewardSound(reward, totalRewards);
                break;
            case 4: // Re coloreados
                // Give reward type 4
                break;
            default:
                break;
        }
        RewardsDataUpdate rewardsDataUpdate = new RewardsDataUpdate(connection);
        rewardsDataUpdate.updateRewardGiven(reward);
    }
}
