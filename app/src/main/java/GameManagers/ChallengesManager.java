package GameManagers;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

import java.text.ParseException;
import java.util.Random;

import Database.Challenges.ChallengesDataAccess;
import Database.Challenges.ChallengesDataUpdate;
import Database.Preferences.PreferencesDataAccess;
import Database.Records.RecordsDataAccess;
import Dates.DateManager;

public class ChallengesManager extends Service {
    private final ChallengesDataAccess challengesDataAccess;
    private final ChallengesDataUpdate challengesDataUpdate;
    private final PreferencesDataAccess preferencesDataAccess;
    private final RecordsDataAccess recordsDataAccess;
    private final DateManager dateManager = new DateManager();

    private static final int MAX_CHALLENGE_NUMBER = 15;
    private static final int MIN_CHALLENGE_NUMBER = 1;

    public ChallengesManager() {
        super();
        challengesDataAccess = null;
        challengesDataUpdate = null;
        preferencesDataAccess = null;
        recordsDataAccess = null;
    }

    public ChallengesManager(ChallengesDataAccess challengesDataAccess,
                             ChallengesDataUpdate challengesDataUpdate,
                             PreferencesDataAccess preferencesDataAccess,
                             RecordsDataAccess recordsDataAccess) {
        this.challengesDataAccess = challengesDataAccess;
        this.challengesDataUpdate = challengesDataUpdate;
        this.preferencesDataAccess = preferencesDataAccess;
        this.recordsDataAccess = recordsDataAccess;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> ((Runnable) () -> {
            while (true) {
                Log.d("ChallengesManagerService", "onStartCommand: Iniciando el servicio");

                Log.d("ChallengesManagerService", "onStartCommand: Terminando el servicio");
            }
        }).run()).start();
        return super.onStartCommand(intent, flags, startId);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void selectNewChallenge() {
        if (challengesDataAccess.allChallengesDisplayed()) {
            challengesDataUpdate.resetChallenges();
        }

        int challenge = getRandomChallenge();
        updateChallengeStatus(challenge);
    }

    private int getRandomChallenge() {
        Random random = new Random();
        int challenge = random.nextInt(MAX_CHALLENGE_NUMBER) + 1;

        while (!challengesDataAccess.isChallengeAvailable(challenge)) {
            challenge = random.nextInt(MAX_CHALLENGE_NUMBER) + 1;
        }

        return challenge;
    }

    private void updateChallengeStatus(int challenge) {
        challengesDataUpdate.markAsDisplayed(challenge);
        challengesDataUpdate.markAsActive(challenge);

        currentChallengeConditions(challenge);
    }

    private void currentChallengeConditions(int challenge) {
        if (challenge >= MIN_CHALLENGE_NUMBER && challenge <= MAX_CHALLENGE_NUMBER) {
            handleChallenge(challenge);
        }
    }

    private void handleChallenge(int challenge) {
        String currentDate = dateManager.getCurrentDate();

        if (challengeConditionsMet(challenge)) {
            int days = challengesDataAccess.getCounter(challenge);
            challengesDataUpdate.updateCounter(challenge, days + 1);
            challengesDataUpdate.updateOldDate(challenge, currentDate);

            if (consecutiveWeek(challenge)) {
                challengesDataUpdate.markAsCompleted(challenge);
            }
        } else {
            challengesDataUpdate.updateCounter(challenge, 0);
            challengesDataUpdate.updateOldDate(challenge, currentDate);
        }
    }

    private boolean challengeConditionsMet(int challenge) {
        switch (challenge) {
            case 1:
                return consecutiveDaysCondition(challenge);
            case 2:
                return preferencesDataAccess.getSaveRecordings() && consecutiveDaysCondition(challenge);
            case 3:
                return preferencesDataAccess.getRecordSnorings() && consecutiveDaysCondition(challenge);
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

    private boolean consecutiveDaysCondition(int challenge) {
        String oldDate = challengesDataAccess.getDate(challenge);
        try {
            return dateManager.compareDates(oldDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean consecutiveWeek(int challenge) {
        return challengesDataAccess.getCounter(challenge) == 7;
    }
}
