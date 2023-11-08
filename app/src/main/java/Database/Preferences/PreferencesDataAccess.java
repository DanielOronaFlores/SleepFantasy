package Database.Preferences;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class PreferencesDataAccess {
    private static SQLiteDatabase database;

    public PreferencesDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();;
    }

    public static void updatePreferences(boolean saveRecordings, boolean recordSnorings) {
        ContentValues values = new ContentValues();
        values.put("saveRecordings", saveRecordings);
        values.put("recordSnorings", recordSnorings);

        if (isPreferencesCreated()) {
            database.update("Preferences", values, null, null);
        } else {
            database.insert("Preferences", null, values);
        }
    }

    public static String[] getPreferencesData() {
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

    public static boolean isPreferencesCreated() {
        String query = "SELECT 1 FROM Preferences LIMIT 1;";
        Cursor cursor = database.rawQuery(query, null);
        boolean preferencesExist = cursor.getCount() > 0;
        cursor.close();
        return preferencesExist;
    }
}
