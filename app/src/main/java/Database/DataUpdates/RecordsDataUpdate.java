package Database.DataUpdates;

import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class RecordsDataUpdate {
    private final SQLiteDatabase database;
    public RecordsDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void updateMonsterAppeared(boolean value) {
        database.execSQL("UPDATE Records SET hasMonsterAppeared = " + value);
    }
    public void updateIsTemporizerActive(boolean value) {
        database.execSQL("UPDATE Records SET isTemporizerActive = " + value);
    }
    public void updateIsPlayingMusic(boolean value) {
        database.execSQL("UPDATE Records SET isPlayingMusic = " + value);
    }
    public void updateIsCategoryValid(boolean value) {
        database.execSQL("UPDATE Records SET isCategoryValid = " + value);
    }
    public void updateHasDeletedAudio(boolean value) {
        database.execSQL("UPDATE Records SET hasDeletedAudio = " + value);
    }
    public void updateHasAvatarVisualChanged(boolean value) {
        database.execSQL("UPDATE Records SET hasAvatarVisualChanged = " + value);
    }
    public void updateIsNewSoundSet(boolean value) {
        database.execSQL("UPDATE Records SET isNewSoundSet = " + value);
    }
    public void updateIsNewInterface(boolean value) {
        database.execSQL("UPDATE Records SET isNewInterface = " + value);
    }
    public void updateIsNewAudioUploaded(boolean value) {
        database.execSQL("UPDATE Records SET isNewAudioUploaded = " + value);
    }
    public void updateHasAudiosPlayed(boolean value) {
        database.execSQL("UPDATE Records SET hasAudiosPlayed = " + value);
    }
    public void updateIsGraphDisplayed(boolean value) {
        database.execSQL("UPDATE Records SET isGraphDisplayed = " + value);
    }
    public void updateHasObtainedExperience(boolean value) {
        database.execSQL("UPDATE Records SET hasObtainedExperience = " + value);
    }
}
