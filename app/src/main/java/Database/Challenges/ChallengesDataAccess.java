package Database.Challenges;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import Database.DatabaseConnection;

public class ChallengesDataAccess {
    private SQLiteDatabase database;

    public ChallengesDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public boolean isChallengeAvaible(int challenge) {
        String query = "SELECT Displayed FROM Challenges WHERE id = " + challenge + ";";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            Log.d("ACCESS", "true");
            cursor.close();
            return true;
        } else {
            Log.d("FALSE", "false");
            cursor.close();
            return false;
        }
    }

    public boolean allChallengesDisplayed() {
        String query = "SELECT Displayed FROM Challenges WHERE Displayed = 0;";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return false;
        } else {
            cursor.close();
            return true;
        }
    }
}