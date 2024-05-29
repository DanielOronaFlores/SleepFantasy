package GameManagers.Challenges;

import android.media.MediaPlayer;

import Clocker.Clock;
import Database.DataAccess.ChallengesDataAccess;
import Database.DataUpdates.ChallengesDataUpdate;
import Database.DataUpdates.RecordsDataUpdate;
import Database.DatabaseConnection;
import Services.AudioPlayer;

public class ChallengesUpdater {
    private final DatabaseConnection connection;
    private final ChallengesDataAccess challengesDataAccess;
    private final ChallengesDataUpdate challengesDataUpdate;
    private final RecordsDataUpdate recordsDataUpdate;

    public ChallengesUpdater(DatabaseConnection connection) {
        this.connection = connection;
        this.challengesDataAccess = new ChallengesDataAccess(connection);
        this.recordsDataUpdate = new RecordsDataUpdate(connection);
        this.challengesDataUpdate = new ChallengesDataUpdate(connection);
    }

    public void updateSleepingConditions() {
        int currentActiveChallenge = challengesDataAccess.getActiveChallenge();
        System.out.println("Current active challenge: " + currentActiveChallenge);

        switch (currentActiveChallenge) {
            case 4:
                MediaPlayer mediaPlayer = AudioPlayer.getMediaPlayer();
                if (mediaPlayer == null) {
                    System.out.println("Media player is null");
                    return;
                }
                System.out.println("Is playing music: " + mediaPlayer.isPlaying());
                recordsDataUpdate.updateIsPlayingMusic(mediaPlayer.isPlaying());
                break;
            case 5:
                Clock clock = new Clock();
                recordsDataUpdate.updateIsTemporizerActive(!clock.getTimeString().equals("00:00"));
                break;
            default:
                break;
        }
    }
    public void updateCategoryRecord(int category) {
        RecordsDataUpdate recordsDataUpdate = new RecordsDataUpdate(connection);
        System.out.println("Category: " + category + " >= 3: " + (category <= 3));
        recordsDataUpdate.updateIsCategoryValid(category <= 3);
    }
    public void updateDeleteAudioRecord() {
        RecordsDataUpdate recordsDataUpdate = new RecordsDataUpdate(connection);
        recordsDataUpdate.updateHasDeletedAudio(true);
    }
    public void updateMonsterAppearedRecord(boolean appeared) {
        RecordsDataUpdate recordsDataUpdate = new RecordsDataUpdate(connection);
        recordsDataUpdate.updateMonsterAppeared(appeared);
    }
    public void updateAvatarVisualRecord() {
        RecordsDataUpdate recordsDataUpdate = new RecordsDataUpdate(connection);
        recordsDataUpdate.updateHasAvatarVisualChanged(true);
    }
    public void updateNotificationSoundRecord() {
        challengesDataUpdate.updateCounter(10, 0);

        RecordsDataUpdate recordsDataUpdate = new RecordsDataUpdate(connection);
        recordsDataUpdate.updateIsNewSoundSet(true);
    }
    public void updateInterfaceRecord() {
        RecordsDataUpdate recordsDataUpdate = new RecordsDataUpdate(connection);
        recordsDataUpdate.updateIsNewInterface(true);
    }
    public void updateAddAudio() {
        RecordsDataUpdate recordsDataUpdate = new RecordsDataUpdate(connection);
        recordsDataUpdate.updateIsNewAudioUploaded(true);
    }
    public void updateLoudSoundsRecord() {
        RecordsDataUpdate recordsDataUpdate = new RecordsDataUpdate(connection);
        recordsDataUpdate.updateHasAudiosPlayed(true);
    }
    public void updateChartRecord() {
        RecordsDataUpdate recordsDataUpdate = new RecordsDataUpdate(connection);
        recordsDataUpdate.updateIsGraphDisplayed(true);
    }
    public void updateExperienceRecord() {
        RecordsDataUpdate recordsDataUpdate = new RecordsDataUpdate(connection);
        recordsDataUpdate.updateHasObtainedExperience(true);
    }
}
