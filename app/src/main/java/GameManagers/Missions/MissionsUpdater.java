package GameManagers.Missions;

public class MissionsUpdater {
    private static MissionsManager missionsManager;

    public MissionsUpdater() {
        missionsManager = new MissionsManager();
    }

    public void updateMission1(int sleepHours) {
        if (sleepHours >= 7 && sleepHours <= 9) {
            missionsManager.updateMission(1, 1);
        }
    }

    public void updateMission2(int awakenings) {
        if (awakenings < 5) {
            missionsManager.updateMission(2, 1);
        }
    }

    public void updateMission3(float efficiency) {
        if (efficiency >= 85) {
            missionsManager.updateMission(3, 1);
        }
    }

    public static void updateMission4(float light) {
        if (light < 100) {
            missionsManager.updateMission(4, 1);
        }
    }

    public void updateMission5() {
        missionsManager.updateMission(5, 1);
    }

    public void updateMission6(int sleepCategory) {
        if (sleepCategory >= 3) {
            missionsManager.updateMission(6, 1);
        }
    }

    public void updateMission7() {
        missionsManager.updateMission(7, 1);
    }

    public void updateMission8(float remSleep) {
        if (remSleep >= 15 && remSleep <= 25) {
            missionsManager.updateMission(8, 1);
        }
    }

    public void updateMission9(int positionChanges) {
        if (positionChanges >= 1) {
            missionsManager.updateMission(9, 1);
        }
    }

    public void updateMission10(int vigilTime) {
        if (vigilTime < 10) {
            missionsManager.updateMission(10, 1);
        }
    }

    public void updateMission11(int positionChanges) {
        if (positionChanges <= 5) {
            missionsManager.updateMission(11, 1);
        }
    }

    public void updateMission12(float lightSleep) {
        if (lightSleep >= 50 && lightSleep <= 60) {
            missionsManager.updateMission(12, 1);
        }
    }

    public void updateMission13() {
        missionsManager.updateMission(13, 1);
    }

    public void updateMission14(int sleepCategory, int requiredCategory) {
        if (sleepCategory >= requiredCategory) {
            missionsManager.updateMission(14, 1);
        }
    }

    public void updateMission15(int events) {
        if (events == 0) {
            missionsManager.updateMission(15, 1);
        }
    }

    public void updateMission16(int suddenMovements) {
        if (suddenMovements <= 5) {
            missionsManager.updateMission(16, 1);
        }
    }

    public void updateMission17(float deepSleep) {
        if (deepSleep >= 20 && deepSleep <= 25) {
            missionsManager.updateMission(17, 1);
        }
    }

    public void updateMission18() {
        missionsManager.updateMission(18, 1);
    }

    public void updateMission19(int tips) {
        if (tips < 5) {
            missionsManager.updateMission(19, 1);
        }
    }
}
