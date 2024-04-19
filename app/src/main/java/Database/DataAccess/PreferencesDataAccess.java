package Database.DataAccess;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class PreferencesDataAccess {
    private final SQLiteDatabase database;

    public PreferencesDataAccess(DatabaseConnection connection) {
        database = connection.getDatabase();
    }

    public boolean getSaveRecordings() {
        String[] columns = {"saveRecordings"};
        Cursor cursor = database.query("Preferences", columns, null, null, null, null, null);
        boolean saveRecordings = false;

        if (cursor.moveToFirst()) {
            saveRecordings = cursor.getInt(0) == 1;
        }

        cursor.close();
        return saveRecordings;
    }

    public boolean getRecordAudios() {
        String[] columns = {"recordAudios"};
        Cursor cursor = database.query("Preferences", columns, null, null, null, null, null);
        boolean recordAudios = false;

        if (cursor.moveToFirst()) {
            recordAudios = cursor.getInt(0) == 1;
        }

        cursor.close();
        return recordAudios;
    }

    public boolean getAudioQuality() {
        String[] columns = {"audioQuality"};
        Cursor cursor = database.query("Preferences", columns, null, null, null, null, null);
        boolean audioQuality = false;

        if (cursor.moveToFirst()) {
            audioQuality = cursor.getInt(0) == 1;
        }

        cursor.close();
        return audioQuality;
    }

    public int getTimerDuration() {
        String[] columns = {"timerDuration"};
        Cursor cursor = database.query("Preferences", columns, null, null, null, null, null);
        int timerDuration = 0;

        if (cursor.moveToFirst()) {
            timerDuration = cursor.getInt(0);
        }

        cursor.close();
        return timerDuration;
    }

    public int getThemeSelected() {
        String[] columns = {"theme"};
        Cursor cursor = database.query("Preferences", columns, null, null, null, null, null);
        int theme = 0;

        if (cursor.moveToFirst()) {
            theme = cursor.getInt(0);
        }

        cursor.close();
        return theme;
    }

    public int getAvatarSkin() {
        String[] columns = {"skin"};
        Cursor cursor = database.query("Preferences", columns, null, null, null, null, null);
        int avatarSkin = 0;

        if (cursor.moveToFirst()) {
            avatarSkin = cursor.getInt(0);
        }

        cursor.close();
        return avatarSkin;
    }


    public int getNotificationSound() {
        String[] columns = {"notification"};
        Cursor cursor = database.query("Preferences", columns, null, null, null, null, null);
        int notificationSound = 0;

        if (cursor.moveToFirst()) {
            notificationSound = cursor.getInt(0);
        }

        cursor.close();
        return notificationSound;
    }
}