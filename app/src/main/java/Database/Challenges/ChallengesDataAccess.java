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

    public boolean isChallengeAvailable(int challenge) {
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
    public int getActiveChallenge() {
        String query = "SELECT id FROM Challenges WHERE Active = 1;";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int challenge = cursor.getInt(0);
            cursor.close();
            return challenge;
        } else {
            cursor.close();
            return 0;
        }
    }

    public String getDate(int challenge) {
        String query = "SELECT OldDate FROM Challenges WHERE id = " + challenge + ";";
        Cursor cursor = database.rawQuery(query, null);
        String date = cursor.getString(0);
        cursor.close();
        return date;
    }

    public int getCounter(int mission) {
        String query = "SELECT Counter FROM Challenges WHERE id = " + mission + ";";
        Cursor cursor = database.rawQuery(query, null);
        int counter = cursor.getInt(0);
        cursor.close();
        return counter;
    }
}