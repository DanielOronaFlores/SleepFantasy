package GameManagers;

import android.util.Log;

import java.text.ParseException;
import java.util.Random;

import Database.Challenges.ChallengesDataAccess;
import Database.Challenges.ChallengesDataUpdate;
import Dates.DatesManager;

public class ChallengesManager {
    private final ChallengesDataAccess challengesDataAccess;
    private final ChallengesDataUpdate challengesDataUpdate;
    private final DatesManager DatesManager = new DatesManager();

    public ChallengesManager(ChallengesDataAccess challengesDataAccess, ChallengesDataUpdate challengesDataUpdate) {
        this.challengesDataAccess = challengesDataAccess;
        this.challengesDataUpdate = challengesDataUpdate;
    }

    public void selectNewChallenge() {
        if (challengesDataAccess.allChallengesDisplayed()) challengesDataUpdate.resetChallenges();
        Random random = new Random();
        int challenge = random.nextInt(15) + 1;
        while (!challengesDataAccess.isChallengeAvaible(challenge)) {
            challenge = random.nextInt(15) + 1;
            Log.d("MANAGER", "NUMERO = " + challenge);
        }
        Log.d("MANAGER", "FIN BUCLE");
        updateChallengeStatus(challenge);
    }

    private void updateChallengeStatus(int challenge) {
        challengesDataUpdate.markAsDisplayed(challenge);
        challengesDataUpdate.markAsActive(challenge);
        Log.d("MANAGER", "STATUS ACTUALIZADOS");
    }

    private void currentChallengeConditions() {
        int currentChallenge = challengesDataAccess.getActiveChallenge();
        switch (currentChallenge) {
            case 1:

                break;
        }
    }

    private boolean consecutiveDaysCondition(int challenge) {
        String oldDate = challengesDataAccess.getDate(challenge);
        try {
            if (DatesManager.compareDates(oldDate)) return true;
            else return false;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean consecutiveWeek(int mission) {
        if (challengesDataAccess.getCounter(mission) == 7) return true;
        else return false;
    }

    //Individual Challenges Conditions
    private void challenge1() {
        int challenge = 1;
        String currentDate = DatesManager.getCurrentDate();

        if (consecutiveDaysCondition(challenge)) {
            int days = challengesDataAccess.getCounter(challenge);
            challengesDataUpdate.updateCounter(challenge, days + 1);
            challengesDataUpdate.updateOldDate(challenge, currentDate);
            if (consecutiveWeek(1)) challengesDataUpdate.markAsCompleted(challenge);
        }
        else {
            challengesDataUpdate.updateCounter(challenge, 0);
            challengesDataUpdate.updateOldDate(challenge, currentDate);
        }
    }
}
