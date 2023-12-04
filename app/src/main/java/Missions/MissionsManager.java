package Missions;

import Database.Missions.MissionDataAccess;
import Database.Missions.MissionDataUpdate;

public class MissionsManager {
    private MissionDataAccess missionDataAccess;
    private MissionDataUpdate missionDataUpdate;

    public MissionsManager(MissionDataAccess missionDataAccess, MissionDataUpdate missionDataUpdate) {
        this.missionDataAccess = missionDataAccess;
        this.missionDataUpdate = missionDataUpdate;
    }

    public void updateMission(int id, int quantity) {
        if (isMissionAvailable(id)) {
            missionDataUpdate.updateCurrentQuantity(id, quantity);
            if (isMissionCompleted(id, quantity)) {
                int difficult = missionDataAccess.getCurrentDifficult(id);
                missionDataUpdate.updateCurrentDifficult(id, difficult + 1); //Aumenta la dificultad de la misión
                updateRequiredQuantity(id, difficult + 1); //Actualiza la cantidad requerida para la siguiente dificultad
            }
        }
        if (missionDataAccess.getCurrentDifficult(id) > 3) {
            missionDataUpdate.updateCompleteStatus(id);
        }
    }
    private boolean isMissionAvailable(int id) {
        return missionDataAccess.getCurrentDifficult(id) <= 3;
    }
    private boolean isMissionCompleted(int id, int currentQuantity) {
        int requiredQuantity = missionDataAccess.getRequiredQuantity(id);
        return currentQuantity >= requiredQuantity;
    }
    private String getMissionType(int id) {
        String missionType;
        switch (id) {
            case 7:
                missionType = "niveles";
                break;
            case 13:
                missionType = "ocasiones";
                break;
            case 18:
                missionType = "monstruos";
                break;
            case 19:
                missionType = "consejos";
                break;
            default:
                missionType = "dias";
        }
        return missionType;
    }
    private void updateRequiredQuantity(int id, int difficult) {
        String missionType = getMissionType(id);
        int newRequiredQuantity;

        switch (missionType) {
            case "niveles":
                if (difficult == 1) newRequiredQuantity = 10;
                else if (difficult == 2) newRequiredQuantity = 25;
                else newRequiredQuantity = 50;
                break;
            case "ocasiones":
                if (difficult == 1) newRequiredQuantity = 1;
                else if (difficult == 2) newRequiredQuantity = 3;
                else newRequiredQuantity = 20;
                break;
            case "monstruos":
                if (difficult == 1) newRequiredQuantity = 3;
                else if (difficult == 2) newRequiredQuantity = 15;
                else newRequiredQuantity = 30;
                break;
            case "consejos":
                if (difficult == 1) newRequiredQuantity = 10;
                else if (difficult == 2) newRequiredQuantity = 5;
                else newRequiredQuantity = 2;
                break;
            case "dias":
                if (difficult == 1) newRequiredQuantity = 3;
                else if (difficult == 2) newRequiredQuantity = 14;
                else newRequiredQuantity = 30;
                break;
            default:
                newRequiredQuantity = 0;
        }

        missionDataUpdate.updateRequiredQuantity(id, newRequiredQuantity);
    }
}
