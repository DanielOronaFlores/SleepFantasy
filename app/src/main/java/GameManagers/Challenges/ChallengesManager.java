package GameManagers.Challenges;

import java.text.ParseException;
import java.util.Random;

import AppContext.MyApplication;
import Database.DataAccess.ChallengesDataAccess;
import Database.DataUpdates.ChallengesDataUpdate;
import Database.DataUpdates.RecordsDataUpdate;
import Database.DatabaseConnection;
import Database.DataAccess.PreferencesDataAccess;
import Database.DataAccess.RecordsDataAccess;
import Dates.DateManager;
import GameManagers.ExperienceManager;

public class ChallengesManager {
    private static ChallengesDataAccess challengesDataAccess;
    private static ChallengesDataUpdate challengesDataUpdate;
    private static PreferencesDataAccess preferencesDataAccess;
    private static RecordsDataAccess recordsDataAccess;
    private static RecordsDataUpdate recordsDataUpdate;
    private static DateManager dateManager;
    private static ExperienceManager experienceManager;
    private static final int MAX_CHALLENGE_NUMBER = 15;
    private static final int MIN_CHALLENGE_NUMBER = 1;

    public ChallengesManager() {
        DatabaseConnection connection = DatabaseConnection.getInstance(MyApplication.getAppContext());

        challengesDataAccess = new ChallengesDataAccess(connection);
        challengesDataUpdate = new ChallengesDataUpdate(connection);
        preferencesDataAccess = new PreferencesDataAccess(connection);
        recordsDataAccess = new RecordsDataAccess(connection);
        recordsDataUpdate = new RecordsDataUpdate(connection);

        dateManager = new DateManager();
        experienceManager = new ExperienceManager();
    }

    public void update() {
        int activeChallenge = challengesDataAccess.getActiveChallenge();

        if (activeChallenge == 0) { // No hay reto activo
            selectNewChallenge();
        } else {
            if (challengesDataAccess.isCompleted(activeChallenge)) { // Si el reto está completado
                selectNewChallenge();
            } else {
                currentChallengeConditions(activeChallenge);
            }
        }
    }

    //Para seleccionar un nuevo reto
    private static boolean isUpdateChallenge() {
        String currentDate = dateManager.getCurrentDate();
        String oldDate = challengesDataAccess.getDate(challengesDataAccess.getActiveChallenge());
        if (oldDate == null) return true;

        return dateManager.isSunday() || dateManager.getDaysDifference(currentDate, oldDate) > 14;
    }

    private static void selectNewChallenge() {
        if (challengesDataAccess.allChallengesDisplayed()) challengesDataUpdate.resetChallenges();

        if (isUpdateChallenge()) {
            int currentActiveChallenge = challengesDataAccess.getActiveChallenge();
            challengesDataUpdate.markAsInactive(currentActiveChallenge);
            recordsDataUpdate.restartAllValues();

            int newChallenge = getRandomChallenge();
            setNewChallenge(newChallenge);
        }
    }
    private static int getRandomChallenge() {
        Random random = new Random();
        int challenge;

        do {
            challenge = random.nextInt(MAX_CHALLENGE_NUMBER) + 1;
        } while (!challengesDataAccess.isChallengeAvailable(challenge));

        return challenge;
    }
    private static void setNewChallenge(int challenge) {
        challengesDataUpdate.markAsDisplayed(challenge);
        challengesDataUpdate.markAsActive(challenge);
    }


    // Para manejar el reto actual
    private static void currentChallengeConditions(int challenge) {
        if (challenge >= MIN_CHALLENGE_NUMBER && challenge <= MAX_CHALLENGE_NUMBER) {
            handleChallenge(challenge);
        }
    }
    private static void handleChallenge(int challenge) {
        String currentDate = dateManager.getCurrentDate();

        if (challengeConditionsMet(challenge)) {
            int days = challengesDataAccess.getCounter(challenge);
            challengesDataUpdate.updateCounter(challenge, days + 1);
            challengesDataUpdate.updateOldDate(challenge, currentDate);

            if (consecutiveWeek(challenge)) {
                challengesDataUpdate.markAsCompleted(challenge);
                experienceManager.addExperience(500);
            }
        } else {
            if (!currentDate.equals(challengesDataAccess.getDate(challenge))) {
                challengesDataUpdate.updateCounter(challenge, 0);
                challengesDataUpdate.updateOldDate(challenge, currentDate);
                recordsDataUpdate.restartAllValues();
            }
        }
    }

    private static boolean challengeConditionsMet(int challenge) {
        switch (challenge) {
            case 1:
                return consecutiveDaysCondition(challenge);
            case 2:
                return preferencesDataAccess.getSaveRecordings() && consecutiveDaysCondition(challenge);
            case 3:
                return preferencesDataAccess.getRecordAudios() && consecutiveDaysCondition(challenge);
            case 4:
                return recordsDataAccess.isPlayingMusic() && consecutiveDaysCondition(challenge);
            case 5:
                return recordsDataAccess.isTemporizerActive() && consecutiveDaysCondition(challenge);
            case 6:
                return recordsDataAccess.hasMonsterAppeared() && consecutiveDaysCondition(challenge);
            case 7:
                return recordsDataAccess.isCategoryValid() && consecutiveDaysCondition(challenge);
            case 8:
                return recordsDataAccess.isDeletedAudio();
            case 9:
                return recordsDataAccess.hasAvatarVisualChanged();
            case 10:
                return recordsDataAccess.isNewSoundSet() && consecutiveDaysCondition(challenge);
            case 11:
                return recordsDataAccess.isNewInterface() && consecutiveDaysCondition(challenge);
            case 12:
                return recordsDataAccess.isNewAudioUploaded() && consecutiveDaysCondition(challenge);
            case 13:
                return recordsDataAccess.hasAudiosPlayed();
            case 14:
                return recordsDataAccess.isGraphDisplayed() && consecutiveDaysCondition(challenge);
            case 15:
                return recordsDataAccess.hasObtainedExperience();
            default:
                return false;
        }
    }
    private static boolean consecutiveDaysCondition(int challenge) {
        String oldDate = challengesDataAccess.getDate(challenge);
        if (oldDate != null)
        {
            try {
                return dateManager.compareDates(oldDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
    private static boolean consecutiveWeek(int challenge) {
        return challengesDataAccess.getCounter(challenge) >= 7;
    }
}
