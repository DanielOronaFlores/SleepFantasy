package GameManagers.Missions;

import AppContext.MyApplication;
import Database.DataAccess.MissionDataAccess;
import Database.DatabaseConnection;

public class MissionsUpdater {
    private static MissionsManager missionsManager;
    private static MissionDataAccess missionDataAccess;

    public MissionsUpdater() {
        missionsManager = new MissionsManager();
        missionDataAccess = new MissionDataAccess(DatabaseConnection.getInstance(MyApplication.getAppContext()));
    }

    public void updateMission1(int sleepMinutes) {
        if (sleepMinutes >= 420 && sleepMinutes <= 540) {
            missionsManager.updateMission(1);
        }
    }

    public void updateMission2(int awakenings) {
        if (awakenings < 5) {
            missionsManager.updateMission(2);
        }
    }

    public void updateMission3(float efficiency) {
        if (efficiency >= 85) {
            missionsManager.updateMission(3);
        }
    }

    public static void updateMission4(float light) {
        if (light < 100) {
            missionsManager.updateMission(4);
        }
    }

    public void updateMission5() {
        missionsManager.updateMission(5);
    }

    public void updateMission6(int sleepCategory) {
        if (sleepCategory <= 3) {
            missionsManager.updateMission(6);
        }
    }

    public void updateMission7() {
        missionsManager.updateMission(7);
    }

    public void updateMission8(float remSleep) {
        if (remSleep >= 15.0f && remSleep <= 25.0f) {
            missionsManager.updateMission(8);
        }
    }

    public void updateMission9(int positionChanges) {
        if (positionChanges <= 1) {
            missionsManager.updateMission(9);
        }
    }

    public void updateMission10(int vigilTime) {
        System.out.println("vigilTime: " + vigilTime);
        if (vigilTime < 10) {
            missionsManager.updateMission(10);
        }
    }

    public void updateMission11(int positionChanges) {
        if (positionChanges <= 5) {
            missionsManager.updateMission(11);
        }
    }

    public void updateMission12(float lightSleep) {
        if (lightSleep >= 50 && lightSleep <= 60) {
            missionsManager.updateMission(12);
        }
    }

    public void updateMission13() {
        missionsManager.updateMission(13);
    }

    public void updateMission14(int sleepCategory, int requiredCategory) {
        if (sleepCategory >= requiredCategory) {
            missionsManager.updateMission(14);
        }
    }

    public void updateMission15(int events) {
        if (events == 0) {
            missionsManager.updateMission(15);
        }
    }

    public void updateMission16(int suddenMovements) {
        if (suddenMovements <= 5) {
            missionsManager.updateMission(16);
        }
    }

    public void updateMission17(float deepSleep) {
        if (deepSleep >= 20 && deepSleep <= 25) {
            missionsManager.updateMission(17);
        }
    }

    public void updateMission18() {
        missionsManager.updateMission(18);
    }

    public void updateMission19() {
        missionsManager.updateMission(19);
    }

    public void updateMission20(int timeInBed) {
        if (timeInBed >= 420) {
            missionsManager.updateMission(20);
        }
    }
}
