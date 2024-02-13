package DataAccess;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import Database.DatabaseConnection;

public class SleepDataAccess {
    private final SQLiteDatabase database;

    public SleepDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public int getVigilTime(String date) {
        String query = "SELECT vigilTime FROM SleepData WHERE date = ?;";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, date);
        Log.d("SleepDataAccess", "getVigilTime: " + statement.simpleQueryForLong());
        return (int) statement.simpleQueryForLong();
    }
    public int getlightSleepTime(String date) {
        String query = "SELECT lightSleepTime FROM SleepData WHERE date = ?;";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, date);
        Log.d("SleepDataAccess", "getlightSleepTime: " + statement.simpleQueryForLong());
        return (int) statement.simpleQueryForLong();
    }
    public int getDeepSleepTime(String date) {
        String query = "SELECT deepSleepTime FROM SleepData WHERE date = ?;";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, date);
        Log.d("SleepDataAccess", "getDeepSleepTime: " + statement.simpleQueryForLong());
        return (int) statement.simpleQueryForLong();
    }
    public int getRemSleepTime(String date) {
        String query = "SELECT REMTime FROM SleepData WHERE date = ?;";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, date);
        Log.d("SleepDataAccess", "getRemSleepTime: " + statement.simpleQueryForLong());
        return (int) statement.simpleQueryForLong();
    }
    public int totalSleepTime(String date) {
        String query = "SELECT totalSleepTime FROM SleepData WHERE date = ?;";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, date);
        Log.d("SleepDataAccess", "totalSleepTime: " + statement.simpleQueryForLong());
        return (int) statement.simpleQueryForLong();
    }
    public int getEfficiency(String date) {
        String query = "SELECT efficiency FROM SleepData WHERE date = ?;";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, date);
        return (int) statement.simpleQueryForLong();
    }
    public int getAwakeningAmount(String date) {
        String query = "SELECT awakeningAmount FROM SleepData WHERE id = ?;";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, date);
        return (int) statement.simpleQueryForLong();
    }
    public int getLoudSoundsAmount(String date) {
        String query = "SELECT loudSoundsAmount FROM SleepData WHERE id = ?;";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, date);
        return (int) statement.simpleQueryForLong();
    }
    public int getSuddenMovementsAmount(String date) {
        String query = "SELECT suddenMovementsAmount FROM SleepData WHERE id = ?;";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, date);
        return (int) statement.simpleQueryForLong();
    }
    public int getSnoringAmount(String date) {
        String query = "SELECT snoringAmount FROM SleepData WHERE id = ?;";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, date);
        return (int) statement.simpleQueryForLong();
    }
    public int getPositionChangesAmount(String date) {
        String query = "SELECT positionChangesAmount FROM SleepData WHERE id = ?;";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, date);
        return (int) statement.simpleQueryForLong();
    }
    public int getSonoringTime(String date) {
        String query = "SELECT sonoringTime FROM SleepData WHERE id = ?;";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindString(1, date);
        return (int) statement.simpleQueryForLong();
    }
}
