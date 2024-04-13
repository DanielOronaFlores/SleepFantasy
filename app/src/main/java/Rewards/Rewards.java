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

        switch (rewardType) {
            case 1: // Audios
                AudiosRewards.giveRewardAudio(reward, connection);
                Notifications.showRewardNotification(reward);
                break;
            case 2: // Interfaz
            case 4: // Re coloreados
                Notifications.showRewardNotification(reward);
                break;
            case 3: // Sonidos Notificacion
                int totalRewards = rewardsDataAccess.getTotalRewards();
                SoundsRewards.giveRewardSound(reward, totalRewards);
                Notifications.showRewardNotification(reward);
                break;
            default:
                break;
        }
        RewardsDataUpdate rewardsDataUpdate = new RewardsDataUpdate(connection);
        rewardsDataUpdate.updateRewardGiven(reward);
    }
}
