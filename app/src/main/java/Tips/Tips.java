package Tips;

import android.content.Context;

import com.example.myapplication.R;

import java.util.Objects;

import AppContext.MyApplication;
import Database.DataAccess.SleepDataAccess;
import Database.DataAccess.TipsDataAccess;
import Database.DataUpdates.TipsDataUpdate;
import Database.DatabaseConnection;
import Dates.DateManager;
import GameManagers.Challenges.ChallengesUpdater;
import GameManagers.Missions.MissionsUpdater;
import Notifications.Notifications;

public class Tips {
    private final SleepDataAccess sleepDataAccess;
    private final TipsDataAccess tipsDataAccess;
    private final TipsDataUpdate tipsDataUpdate;
    private final MissionsUpdater missionsUpdater;
    private final Context context;

    public Tips() {
        context = MyApplication.getAppContext();
        DatabaseConnection connection = DatabaseConnection.getInstance(context);
        sleepDataAccess = new SleepDataAccess(connection);
        tipsDataAccess = new TipsDataAccess(connection);
        tipsDataUpdate = new TipsDataUpdate(connection);
        missionsUpdater = new MissionsUpdater();
    }

    public void updateTip() {
        String lastDateAppeared = tipsDataAccess.getLastDateAppeared();
        if (lastDateAppeared == null || tipsDataAccess.getCurrentTipType() == -1) {
            selectTip();
            return;
        }

        long daysDifference = Math.abs(DateManager.getDaysDifference(DateManager.getCurrentDate(), lastDateAppeared));
        System.out.println("Last date appeared: " + lastDateAppeared);
        System.out.println("Days difference: " + daysDifference);

        if (daysDifference >= 3) {
            selectTip();
        } else
            System.out.println("No se puede mostrar un nuevo tip debido a que ya se mostró uno recientemente.");
    }

    public void selectTip() {
        int sleepEventType = getSleepEventType(); // 0: No hay eventos, 1: Despertares, 2: Cambios de posición, 3: Movimientos bruscos
        System.out.println("Sleep event type: " + sleepEventType);
        boolean isSleepTimeDecreased = isDecreased();
        System.out.println("Is sleep time decreased: " + isSleepTimeDecreased);
        boolean isHighLux = getLuxValue() > 50;
        System.out.println("Is high lux: " + isHighLux);

        boolean[] tipsConditions = {sleepEventType > 0, isSleepTimeDecreased, isHighLux};
        int lastTipType = tipsDataAccess.getCurrentTipType();
        int tipType = selectTipType(tipsConditions, lastTipType);

        System.out.println("Conditions " + tipsConditions[0] + " " + tipsConditions[1] + " " + tipsConditions[2]);
        System.out.println("Last tip type: " + lastTipType);
        System.out.println("Tip type: " + tipType);

        if (tipType == 0) {  // No hay tips disponibles
            //missionsUpdater.updateMission13();
            //missionsUpdater.updateMission19(tipsConditions.length);
            System.out.println("No hay tips disponibles");
            return;
        }

        String[] tips = switch (tipType) {
            case 1 -> context.getResources().getStringArray(R.array.tips_sleepEvents);
            case 2 -> context.getResources().getStringArray(R.array.tips_sleepTime);
            case 3 -> context.getResources().getStringArray(R.array.tips_sleepEnvironment);
            default -> null;
        };

        tipsDataUpdate.updateCurrentTip(lastTipType, false);
        tipsDataUpdate.updateCurrentTip(tipType, true);
        tipsDataUpdate.updateLastDateAppeared(DateManager.getCurrentDate());

        int tipIndex = (int) (Math.random() * Objects.requireNonNull(tips).length);
        Notifications.showTipNotification(tips[tipIndex]);

        tipsDataUpdate.updateCurrentTipId(tipIndex);
        tipsDataUpdate.updateDisplayed(false);

        System.out.println("Tip: " + tips[tipIndex]);
    }

    private int getSleepEventType() {
        int awakenings = 0, positionChanges = 0, suddenMovements = 0;

        for (int i = 0; i < 3; i++) {
            String date = DateManager.getPastDaySinceCurrentDate(i);
            awakenings += sleepDataAccess.getAwakenings(date);
            positionChanges += sleepDataAccess.getPositionChanges(date);
            suddenMovements += sleepDataAccess.getSuddenMovements(date);
        }

        if (awakenings < 5 && positionChanges < 5 && suddenMovements < 5) {
            return 0;
        } else {
            return selectBiggerEvent(awakenings, positionChanges, suddenMovements);
        }
    }

    private int selectBiggerEvent(int awakenings, int positionChanges, int suddenMovements) {
        if (awakenings > positionChanges && awakenings > suddenMovements) {
            return 1;
        } else if (positionChanges > awakenings && positionChanges > suddenMovements) {
            return 2;
        } else {
            return 3;
        }
    }

    private boolean isDecreased() {
        int sleepTime = sleepDataAccess.getTotalSleepTime(DateManager.getCurrentDate());
        System.out.println("Sleep time: " + sleepTime);

        for (int i = 0; i < 3; i++) {
            String date = DateManager.getPastDaySinceCurrentDate(i + 1);
            System.out.println("Date: " + date);

            int sleepTimePast = sleepDataAccess.getTotalSleepTime(date);
            System.out.println("Sleep time past: " + sleepTimePast);
            if (sleepTimePast < sleepTime && sleepTimePast < 420) return true;
        }
        return false;
    }

    private int getLuxValue() {
        int lux = 0;

        for (int i = 0; i < 3; i++) {
            String date = DateManager.getPastDaySinceCurrentDate(i);
            lux += sleepDataAccess.getLux(date);
        }

        return lux / 3;
    }

    private int selectTipType(boolean[] tipsConditions, int lastTipType) {
        int counterConditionsType = 0;
        int tipType = 0;
        boolean isTipAvaible = false;

        for (boolean tipsCondition : tipsConditions) {
            if (tipsCondition) {
                isTipAvaible = true;
                break;
            }
        }
        if (!isTipAvaible) return 0;

        for (int i = 0; i < tipsConditions.length; i++) {
            if (tipsConditions[i]) {
                counterConditionsType++;
                tipType = i + 1;
            }
        }

        if (counterConditionsType == 1) return tipType;

        do {
            tipType = (int) (Math.random() * tipsConditions.length) + 1;
        } while (tipType == lastTipType || !tipsConditions[tipType - 1]);

        return tipType;
    }
}