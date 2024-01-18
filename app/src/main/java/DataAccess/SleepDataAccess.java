package DataAccess;

import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class SleepDataAccess {
    private final SQLiteDatabase database;

    public SleepDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

}
