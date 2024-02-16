package DataAccess;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Database.DatabaseConnection;
import Dates.DateManager;

public class SleepDataAccess {
    private final SQLiteDatabase database;
    private final DateManager dateManager = new DateManager();
    String startDate;

    public SleepDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    //Use in Pie Charts
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

    //Use in Bar Charts
    public int[] getEfficiency(String date, boolean isWeek) {
        startDate = getStartDate(date, isWeek);

        String query = "SELECT efficiency FROM SleepData WHERE date BETWEEN ? AND ?;";
        Cursor cursor = database.rawQuery(query, new String[]{startDate, date});

        return getQueryArray(cursor);
    }

    public int[] getAwakeningAmount(String date, boolean isWeek) {
        startDate = getStartDate(date, isWeek);

        String query = "SELECT awakeningAmount FROM SleepData WHERE date BETWEEN ? AND ?;";
        Cursor cursor = database.rawQuery(query, new String[]{startDate, date});

        return getQueryArray(cursor);
    }
    public int[] getLoudSoundsAmount(String date, boolean isWeek) {
        startDate = getStartDate(date, isWeek);

        String query = "SELECT loudSoundsAmount FROM SleepData WHERE date BETWEEN ? AND ?;";
        Cursor cursor = database.rawQuery(query, new String[]{startDate, date});

        return getQueryArray(cursor);
    }
    public int[] getSuddenMovementsAmount(String date, boolean isWeek) {
        startDate = getStartDate(date, isWeek);

        String query = "SELECT suddenMovementsAmount FROM SleepData WHERE date BETWEEN ? AND ?;";
        Cursor cursor = database.rawQuery(query, new String[]{startDate, date});

        return getQueryArray(cursor);
    }
    public int[] getPositionChangesAmount(String date, boolean isWeek) {
        startDate = getStartDate(date, isWeek);

        String query = "SELECT positionChangesAmount FROM SleepData WHERE date BETWEEN ? AND ?;";
        Cursor cursor = database.rawQuery(query, new String[]{startDate, date});

        return getQueryArray(cursor);
    }

    //Other methods
    private String getStartDate(String date, boolean isWeek) {
        if (isWeek) return dateManager.getPastWeek(date);
        else return dateManager.getPastMonth(date);
    }
    private int[] getQueryArray(Cursor cursor) {
        int[] array = new int[cursor.getCount()];
        int index = 0;

        while (cursor.moveToNext()) {
            int efficiency = cursor.getInt(0);
            array[index++] = efficiency;
        }

        cursor.close();
        return array;
    }
}
