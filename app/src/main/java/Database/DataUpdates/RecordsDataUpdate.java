package Database.DataUpdates;

import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class RecordsDataUpdate {
    private final SQLiteDatabase database;
    public RecordsDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void updateMonsterAppeared() {
        database.execSQL("UPDATE Records SET hasMonsterAppeared = 1");
    }
}
