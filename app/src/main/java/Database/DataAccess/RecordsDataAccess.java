package Database.DataAccess;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class RecordsDataAccess {
    private final SQLiteDatabase database;

    public RecordsDataAccess(DatabaseConnection connection) {
        database = connection.getDatabase();;
    }

    public boolean isPlayingMusic() {
        String columnName = "isPlayingMusic";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean playingMusic = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            playingMusic = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return playingMusic;
    }
    public boolean isTemporizerActive() {
        String columnName = "isTemporizerActive";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean temporizerActive = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            temporizerActive = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return temporizerActive;
    }
    public boolean hasMonsterAppeared() {
        String columnName = "hasMonsterAppeared";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean monsterAppeared = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            monsterAppeared = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return monsterAppeared;
    }
    public boolean isCategoryValid() {
        String columnName = "isCategoryValid";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean categoryValid = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            categoryValid = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return categoryValid;
    }
    public boolean isDeletedAudio() {
        String columnName = "isDeletedAudio";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean deletedAudio = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            deletedAudio = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return deletedAudio;
    }
    public boolean hasAvatarVisualChanged() {
        String columnName = "hasAvatarVisualChanged";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean avatarVisualChanged = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            avatarVisualChanged = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return avatarVisualChanged;
    }
    public boolean isNewSoundSet() {
        String columnName = "isNewSoundSet";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean newSoundSet = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            newSoundSet = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return newSoundSet;
    }
    public boolean isNewInterface() {
        String columnName = "isNewInterface";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean newInterface = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            newInterface = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return newInterface;
    }
    public boolean isNewAudioUploaded() {
        String columnName = "isNewAudioUploaded";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean newAudioUploaded = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            newAudioUploaded = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return newAudioUploaded;
    }
    public boolean hasAudiosPlayed() {
        String columnName = "hasAudiosPlayed";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean audiosPlayed = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            audiosPlayed = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return audiosPlayed;
    }
    public boolean isGraphDisplayed() {
        String columnName = "isGraphDisplayed";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean graphDisplayed = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            graphDisplayed = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return graphDisplayed;
    }
    public boolean hasObtainedExperience() {
        String columnName = "hasObtainedExperience";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean obtainedExperience = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            obtainedExperience = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return obtainedExperience;
    }

    public void restartAllValues() {
        database.execSQL("UPDATE Records SET isPlayingMusic = 0, " +
                "isTemporizerActive = 0, " +
                "hasMonsterAppeared = 0, " +
                "isCategoryValid = 0, " +
                "hasDeletedAudio = 0, " +
                "hasAvatarVisualChanged = 0, " +
                "isNewSoundSet = 0, " +
                "isNewInterface = 0, " +
                "isNewAudioUpoloaded    = 0, " +
                "hasAudiosPlayed = 0, " +
                "isGraphDisplayed = 0, " +
                "hasObtainedExperience = 0");
    }
}