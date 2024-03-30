package Database.DataAccess;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class ProbabilitiesDataAccess {
    private final SQLiteDatabase database;

    public ProbabilitiesDataAccess(DatabaseConnection connection) {
        database = connection.getDatabase();
    }

    public float getProbability(String category, int range, String attribute) {
        Cursor cursor = null;
        float probability = 0;

        try {
            String query = "SELECT " + category + " FROM Probabilities WHERE range = ? AND attributeName = ?";
            String[] selectionArgs = {String.valueOf(range), attribute};

            cursor = database.rawQuery(query, selectionArgs);

            if (cursor != null && cursor.moveToFirst()) {
                probability = cursor.getFloat(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return probability;
    }
}
