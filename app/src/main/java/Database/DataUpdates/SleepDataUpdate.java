package Database.DataUpdates;

import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class SleepDataUpdate {
    private final SQLiteDatabase database;

    public SleepDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    //TODO: Tal vez sea placeHolder
    public void addLightValue(float lightValue) {
        String query = "INSERT INTO SleepData (lightSleepTime) VALUES (" + lightValue + ");";
        database.execSQL(query);
    }
}
