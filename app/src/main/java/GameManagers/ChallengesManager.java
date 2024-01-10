package GameManagers;

import android.util.Log;
import android.widget.Toast;

import java.util.Random;

import Database.Challenges.ChallengesDataAccess;
import Database.Challenges.ChallengesDataUpdate;

public class ChallengesManager {
    private final ChallengesDataAccess challengesDataAccess;
    private final ChallengesDataUpdate challengesDataUpdate;

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
}
