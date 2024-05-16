package GameManagers.Missions;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

import AppContext.MyApplication;
import Database.DataAccess.MissionDataAccess;
import Database.DataUpdates.MissionDataUpdate;
import Database.DatabaseConnection;
import Dates.DateManager;
import GameManagers.ExperienceManager;

public class MissionsManager {
    DatabaseConnection connection;
    private final MissionDataAccess missionDataAccess;
    private final MissionDataUpdate missionDataUpdate;
    private final ExperienceManager systemExperience;

    public MissionsManager() {
        connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
        connection.openDatabase();

        missionDataAccess = new MissionDataAccess(connection);
        missionDataUpdate = new MissionDataUpdate(connection);
        systemExperience = new ExperienceManager();
    }

    public void updateMission(int id) {
        System.out.println("Mision ID: " + id);
        System.out.println("Mision disponible: " + isMissionAvailable(id));
        if (isMissionAvailable(id)) {
            String missionType = getMissionType(id);
            System.out.println("Tipo de mision: " + missionType);

            boolean areConsecutive = areConsecutiveDays(id);
            System.out.println("Dias conesecutivos: " + areConsecutive);

            missionDataUpdate.updateDate(id);
            if (missionType.equals("dias") && !areConsecutive) {
                return;
            }

            int currentQuantity = missionDataAccess.getCurrentQuantity(id);
            int newQuantity = currentQuantity + 1;
            missionDataUpdate.updateCurrentQuantity(id, newQuantity);

            if (isMissionCompleted(id, newQuantity)) {
                addExperience(missionDataAccess.getCurrentDifficult(id));
                increaseMissionDifficulty(id);
            }
        }
        checkAndUpdateMissionStatus(id);
    }

    private boolean areConsecutiveDays(int id) {
        String oldDay = missionDataAccess.getDate(id);
        return DateManager.isConsecutiveDays(oldDay);
    }

    private void checkAndUpdateMissionStatus(int id) {
        if (missionDataAccess.getCurrentDifficult(id) >= 3) {
            missionDataUpdate.updateCompleteStatus(id);
        }
    }

    private void increaseMissionDifficulty(int id) {
        int difficult = missionDataAccess.getCurrentDifficult(id);
        missionDataUpdate.updateCurrentDifficult(id, difficult + 1);
        updateRequiredQuantity(id, difficult + 1);
    }

    private void addExperience(int difficult) {
        int experience;
        switch (difficult) {
            case 1:
                experience = 50;
                break;
            case 2:
                experience = 200;
                break;
            case 3:
                experience = 500;
                break;
            default:
                experience = 0;
        }
        systemExperience.addExperience(experience);
    }
    private boolean isMissionAvailable(int id) {
        return missionDataAccess.getCurrentDifficult(id) <= 3;
    }
    private boolean isMissionCompleted(int id, int currentQuantity) {
        int requiredQuantity = missionDataAccess.getRequiredQuantity(id);
        System.out.println("Cantidad actual: " + currentQuantity);
        System.out.println("Cantidad requerida: " + requiredQuantity);
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
