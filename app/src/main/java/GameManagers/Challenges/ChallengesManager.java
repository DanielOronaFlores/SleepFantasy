package GameManagers.Challenges;

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
    private static final int MAX_CHALLENGE_NUMBER = 15;
    private static final int MIN_CHALLENGE_NUMBER = 1;
    private static ChallengesDataAccess challengesDataAccess;
    private static ChallengesDataUpdate challengesDataUpdate;
    private static PreferencesDataAccess preferencesDataAccess;
    private static RecordsDataAccess recordsDataAccess;
    private static RecordsDataUpdate recordsDataUpdate;
    private static ExperienceManager experienceManager;

    public ChallengesManager() {
        DatabaseConnection connection = DatabaseConnection.getInstance(MyApplication.getAppContext());

        challengesDataAccess = new ChallengesDataAccess(connection);
        challengesDataUpdate = new ChallengesDataUpdate(connection);
        preferencesDataAccess = new PreferencesDataAccess(connection);
        recordsDataAccess = new RecordsDataAccess(connection);
        recordsDataUpdate = new RecordsDataUpdate(connection);

        experienceManager = new ExperienceManager();
    }

    private static void selectNewChallenge() {
        if (challengesDataAccess.allChallengesDisplayed()) challengesDataUpdate.resetChallenges();

        int currentActiveChallenge = challengesDataAccess.getActiveChallenge();
        challengesDataUpdate.markAsInactive(currentActiveChallenge);
        recordsDataUpdate.restartAllValues();

        int newChallenge = getRandomChallenge();
        setNewChallenge(newChallenge);

        challengesDataUpdate.updateStartDate(newChallenge, DateManager.getCurrentDate());
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
        String currentDate = DateManager.getCurrentDate();
        System.out.println("Se cumplio la condicion del reto " + challenge + ": " + challengeConditionsMet(challenge));
        if (challengeConditionsMet(challenge)) {
            int days = challengesDataAccess.getCounter(challenge);
            System.out.println("Dias del reto (anterior) " + days);
            challengesDataUpdate.updateCounter(challenge, days + 1);
            challengesDataUpdate.updateOldDate(challenge, currentDate);

            if (isOneIterationChallenge(challenge)) {
                challengesDataUpdate.markAsCompleted(challenge);
                experienceManager.addExperience(150);
            } else if (consecutiveWeek(challenge)) {
                challengesDataUpdate.markAsCompleted(challenge);
                experienceManager.addExperience(150);
            }
        } else {
            if (!currentDate.equals(challengesDataAccess.getOldDate(challenge))) {
                System.out.println("Se reinicio el reto " + challenge);

                challengesDataUpdate.updateCounter(challenge, 0);
                challengesDataUpdate.updateOldDate(challenge, currentDate);
                recordsDataUpdate.restartAllValues();
            }
        }
    }

    private static boolean challengeConditionsMet(int challenge) {
        return switch (challenge) {
            case 1 -> consecutiveDaysCondition(challenge);
            case 2 ->
                    preferencesDataAccess.getRecordAudios() && consecutiveDaysCondition(challenge);
            case 3 ->
                    preferencesDataAccess.getSaveRecordings() && consecutiveDaysCondition(challenge);
            case 4 -> recordsDataAccess.isPlayingMusic() && consecutiveDaysCondition(challenge);
            case 5 -> recordsDataAccess.isTemporizerActive() && consecutiveDaysCondition(challenge);
            case 6 -> recordsDataAccess.hasMonsterAppeared() && consecutiveDaysCondition(challenge);
            case 7 -> recordsDataAccess.isCategoryValid() && consecutiveDaysCondition(challenge);
            case 8 -> recordsDataAccess.isDeletedAudio();
            case 9 -> recordsDataAccess.hasAvatarVisualChanged() && consecutiveDaysCondition(challenge);
            case 10 -> recordsDataAccess.isNewSoundSet() && consecutiveDaysCondition(challenge);
            case 11 -> recordsDataAccess.isNewInterface() && consecutiveDaysCondition(challenge);
            case 12 ->
                    recordsDataAccess.isNewAudioUploaded() && consecutiveDaysCondition(challenge);
            case 13 -> recordsDataAccess.hasAudiosPlayed();
            case 14 -> recordsDataAccess.isGraphDisplayed() && consecutiveDaysCondition(challenge);
            case 15 -> recordsDataAccess.hasObtainedExperience();
            default -> false;
        };
    }

    private static boolean isOneIterationChallenge(int challenge) {
        return challenge == 8 || challenge == 13 || challenge == 15;
    }

    private static boolean consecutiveDaysCondition(int challenge) {
        String oldDate = challengesDataAccess.getOldDate(challenge);
        System.out.println("oldDate: " + oldDate);
        if (oldDate != null) {
            return DateManager.isConsecutive(DateManager.getCurrentDate(), oldDate);
        }
        return false;
    }

    private static boolean consecutiveWeek(int challenge) {
        return challengesDataAccess.getCounter(challenge) >= 7;
    }

    public void manageChallenges() {
        System.out.println("-------------Desafios-------------");
        int activeChallenge = challengesDataAccess.getActiveChallenge();
        System.out.println("activeChallenge: " + activeChallenge);

        if (activeChallenge == 0) { // No hay reto activo
            selectNewChallenge();
        } else {
            String startDate = challengesDataAccess.getStartDate(activeChallenge);
            if (DateManager.getDaysDifference(DateManager.getCurrentDate(), startDate) > 15) {
                selectNewChallenge();
            }

            System.out.println("isCompleted: " + challengesDataAccess.isCompleted(activeChallenge));
            if (challengesDataAccess.isCompleted(activeChallenge)) { // Si el reto está completado
                selectNewChallenge();
            } else {
                currentChallengeConditions(activeChallenge);
            }
        }
    }
}
