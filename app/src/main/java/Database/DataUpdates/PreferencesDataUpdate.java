package Database.DataUpdates;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class PreferencesDataUpdate {
    private final SQLiteDatabase database;

    public PreferencesDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void setAvatarSkin(int avatarSkin) {
        ContentValues values = new ContentValues();
        values.put("skin", avatarSkin);

        database.update("Preferences", values, null, null);
    }

    public void updatePreferences(boolean saveRecordings, boolean recordAudios) {
        ContentValues values = new ContentValues();
        values.put("saveRecordings", saveRecordings);
        values.put("recordAudios", recordAudios);

        database.update("Preferences", values, null, null);
    }

    public void updateAudioQuality(boolean audioQuality) {
        ContentValues values = new ContentValues();
        values.put("audioQuality", audioQuality);

        database.update("Preferences", values, null, null);
    }

    public void setTimerDuration(int timerDuration) {
        ContentValues values = new ContentValues();
        values.put("timerDuration", timerDuration);

        database.update("Preferences", values, null, null);
    }

    public void setNotificationSound(int notificationSound) {
        ContentValues values = new ContentValues();
        values.put("notification", notificationSound);

        database.update("Preferences", values, null, null);
    }

    public void setTheme(int theme) {
        ContentValues values = new ContentValues();
        values.put("theme", theme);

        database.update("Preferences", values, null, null);
    }
}
