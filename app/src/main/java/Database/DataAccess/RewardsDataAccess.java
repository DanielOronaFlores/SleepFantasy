package Database.DataAccess;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Database.DatabaseConnection;

public class RewardsDataAccess {
    private final SQLiteDatabase database;

    public RewardsDataAccess(DatabaseConnection connection) {
        database = connection.getDatabase();;
    }

    public List<Integer> getAllUngivenRewards() {
        String columnName = "id";
        Cursor cursor = database.query("Rewards", new String[]{columnName}, "given = 0", null, null, null, null);
        List<Integer> rewards = new ArrayList<>();

        while (cursor.moveToNext()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            rewards.add(cursor.getInt(columnIndex));
        }

        cursor.close();
        return rewards;
    }

    public int getRewardType(int id) {
        String columnName = "type";
        Cursor cursor = database.query("Rewards", new String[]{columnName}, "id = " + id, null, null, null, null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            int type = cursor.getInt(columnIndex);
            cursor.close();
            return type;
        } else {
            cursor.close();
            return 0;
        }
    }
}
