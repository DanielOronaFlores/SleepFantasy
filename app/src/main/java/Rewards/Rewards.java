package Rewards;

import java.util.List;

import AppContext.MyApplication;
import Database.DataAccess.RewardsDataAccess;
import Database.DataUpdates.RewardsDataUpdate;
import Database.DatabaseConnection;
import Notifications.Notifications;

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

        if (rewardType == 1) {
            AudiosRewards.giveRewardAudio(reward, connection);
        }

        RewardsDataUpdate rewardsDataUpdate = new RewardsDataUpdate(connection);
        rewardsDataUpdate.updateRewardGiven(reward);
        Notifications.showRewardNotification(reward);
    }
}
