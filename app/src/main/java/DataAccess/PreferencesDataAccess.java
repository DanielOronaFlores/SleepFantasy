package DataAccess;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class PreferencesDataAccess {
    private final SQLiteDatabase database;

    public PreferencesDataAccess(DatabaseConnection connection) {
        database = connection.getDatabase();
    }

    public void updatePreferences(boolean saveRecordings, boolean recordSnorings) {
        ContentValues values = new ContentValues();
        values.put("saveRecordings", saveRecordings);
        values.put("recordSnorings", recordSnorings);

        if (isPreferencesCreated()) {
            database.update("Preferences", values, null, null);
        } else {
            database.insert("Preferences", null, values);
        }
    }

    public void updateAudioQuality(boolean audioQuality) {
        ContentValues values = new ContentValues();
        values.put("audioQuality", audioQuality);

        if (isPreferencesCreated()) {
            database.update("Preferences", values, null, null);
        } else {
            database.insert("Preferences", null, values);
        }
    }

    public void setTimerDuration(int timerDuration) {
        ContentValues values = new ContentValues();
        values.put("timerDuration", timerDuration);

        if (isPreferencesCreated()) {
            database.update("Preferences", values, null, null);
        } else {
            database.insert("Preferences", null, values);
        }
    }

    public String[] getPreferencesData() {
        String[] columns = {"saveRecordings", "recordSnorings"};
        Cursor cursor = database.query("Preferences", columns, null, null, null, null, null);
        String[] preferences = new String[2];

        if (cursor.moveToFirst()) {
            preferences[0] = String.valueOf(cursor.getInt(0));
            preferences[1] = String.valueOf(cursor.getInt(1));
        }

        cursor.close();
        return preferences;
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

    public boolean getRecordSnorings() {
        String[] columns = {"recordSnorings"};
        Cursor cursor = database.query("Preferences", columns, null, null, null, null, null);
        boolean recordSnorings = false;

        if (cursor.moveToFirst()) {
            recordSnorings = cursor.getInt(0) == 1;
        }

        cursor.close();
        return recordSnorings;
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

    public void setDefaultAudioQuality() {
        ContentValues values = new ContentValues();
        values.put("audioQuality", false);
        database.insert("Preferences", null, values);
    }

    public boolean isPreferencesCreated() {
        String query = "SELECT 1 FROM Preferences LIMIT 1;";
        Cursor cursor = database.rawQuery(query, null);
        boolean preferencesExist = cursor.getCount() > 0;
        cursor.close();
        return preferencesExist;
    }
}
