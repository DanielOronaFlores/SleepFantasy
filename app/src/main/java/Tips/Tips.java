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
       if (DateManager.getDaysDifference(DateManager.getCurrentDate(), lastDateAppeared) > 3) {
           selectTip();
       } else System.out.println("No se puede mostrar un nuevo tip debido a que ya se mostró uno recientemente.");
    }

    public void selectTip() {
        int sleepEventType = getSleepEventType(); // 0: No hay eventos, 1: Despertares, 2: Cambios de posición, 3: Movimientos bruscos
        boolean isSleepTimeDecreased = isDecreased();
        boolean isHighLux = getLuxValue() > 50;

        boolean[] tipsConditions = {sleepEventType > 0, isSleepTimeDecreased, isHighLux};
        int lastTipType = tipsDataAccess.getCurrentTipType();
        int tipType = selectTipType(tipsConditions, lastTipType);

        System.out.println("Conditions " + tipsConditions[0] + " " + tipsConditions[1] + " " + tipsConditions[2]);
        System.out.println("Last tip type: " + lastTipType);
        System.out.println("Tip type: " + tipType);

        if (tipType == 0) {  // No hay tips disponibles
            missionsUpdater.updateMission19();
            return;
        }

        String[] tips = null;
        switch (tipType) {
            case 1:
                tips = context.getResources().getStringArray(R.array.tips_sleepEvents);
                break;
            case 2:
                tips = context.getResources().getStringArray(R.array.tips_sleepTime);
                break;
            case 3:
                tips = context.getResources().getStringArray(R.array.tips_sleepEnvironment);
                break;
        }

        tipsDataUpdate.updateCurrentTip(lastTipType, false);
        tipsDataUpdate.updateCurrentTip(tipType, true);
        tipsDataUpdate.updateLastDateAppeared(DateManager.getCurrentDate());

        int tipIndex = (int) (Math.random() * Objects.requireNonNull(tips).length);
        Notifications.showTipNotification(tips[tipIndex]);
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

        for (int i = 0; i < 3; i++) {
            String date = DateManager.getPastDaySinceCurrentDate(i);
            int sleepTimePast = sleepDataAccess.getTotalSleepTime(date);
            if (sleepTimePast > sleepTime && sleepTimePast < 420) {
                return true;
            }
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