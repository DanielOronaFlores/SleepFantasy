package Database.DataUpdates;

import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class RewardsDataUpdate {
    private SQLiteDatabase database;

    public RewardsDataUpdate(DatabaseConnection connection) {
        database = connection.getDatabase();
    }

    public void updateRewardGiven(int id) {
        database.execSQL("UPDATE Rewards SET given = 1 WHERE id = " + id);
    }
}
