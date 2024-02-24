package DataAccess;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import AppContext.MyApplication;
import Database.DatabaseConnection;

public class ChallengesDataAccess {
    private final SQLiteDatabase database;

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

    public boolean isCompleted(int challenge) {
        String query = "SELECT Completed FROM Challenges WHERE id = " + challenge + ";";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            boolean completed = cursor.getInt(0) == 1;
            cursor.close();
            return completed;
        } else {
            cursor.close();
            return false;
        }
    }

    public boolean isDisplayed(int challenge) {
        String query = "SELECT Displayed FROM Challenges WHERE id = " + challenge + ";";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            boolean displayed = cursor.getInt(0) == 1;
            cursor.close();
            return displayed;
        } else {
            cursor.close();
            return false;
        }
    }

    public String getDate(int challenge) {
        String query = "SELECT OldDate FROM Challenges WHERE id = " + challenge + ";";
        Cursor cursor = database.rawQuery(query, null);

        String date = null;

        try {
            if (cursor.moveToFirst()) date = cursor.getString(0);
        } catch (Exception e) {
            Log.e("ChallengesDataAccess", "Error al obtener la fecha", e);
        } finally {
            cursor.close();
        }

        return date;
    }


    public int getCounter(int mission) {
        int counter = -1; // Valor predeterminado en caso de que no haya resultados

        String query = "SELECT Counter FROM Challenges WHERE id = " + mission + ";";
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                counter = cursor.getInt(cursor.getColumnIndexOrThrow("Counter"));
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return counter;
    }

}