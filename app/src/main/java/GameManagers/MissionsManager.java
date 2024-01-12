package GameManagers;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

import AppContext.MyApplication;
import DataAccess.MissionDataAccess;
import DataUpdates.MissionDataUpdate;
import Database.DatabaseConnection;

public class MissionsManager {
    private final MissionDataAccess missionDataAccess;
    private final MissionDataUpdate missionDataUpdate;
    private final ExperienceManager systemExperience;

    public MissionsManager() {
        DatabaseConnection connection = DatabaseConnection.getInstance(MyApplication.getAppContext());

        missionDataAccess = new MissionDataAccess(connection);
        missionDataUpdate = new MissionDataUpdate(connection);
        systemExperience = new ExperienceManager();
    }

    public void updateMission(int id, int quantity) {
        if (isMissionAvailable(id)) {
            String missionType = getMissionType(id);
            if (missionType.equals("dias") && !areConsecutiveDays(id)) {
                return;
            }

            missionDataUpdate.updateDate(id);
            missionDataUpdate.updateCurrentQuantity(id, quantity);

            if (isMissionCompleted(id, quantity)) {
                addExperience(missionDataAccess.getCurrentDifficult(id));
                increaseMissionDifficulty(id);
            }
        }
        checkAndUpdateMissionStatus(id);
    }

    private boolean areConsecutiveDays(int id) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        String oldDay = dateFormat.format(date);

        LocalDate localDate1;
        LocalDate localDate2;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            localDate1 = LocalDate.parse(oldDay);
            localDate2 = LocalDate.parse(missionDataAccess.getDate(id));

            return localDate2.isEqual(localDate1.plusDays(1));
        }
        return false;
    }

    private void checkAndUpdateMissionStatus(int id) {
        if (missionDataAccess.getCurrentDifficult(id) > 3) {
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
