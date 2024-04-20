package Database.DataUpdates;

import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class TipsDataUpdate {
    private final SQLiteDatabase database;
    public TipsDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void updateCurrentTip(int tipType, boolean active) {
        String query = "UPDATE Tips SET current = ? WHERE type = ?";
        database.execSQL(query, new String[]{active ? "1" : "0", String.valueOf(tipType)});
    }

    public void updateLastDateAppeared(String date) {
        String query = "UPDATE Tips SET lastDateAppeared = ?";
        database.execSQL(query, new String[]{date});
    }
}
