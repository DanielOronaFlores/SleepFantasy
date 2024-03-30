package Database.DataUpdates;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

import Calculators.Efficiency;
import Database.DatabaseConnection;
import Dates.DateManager;

public class SleepDataUpdate {
    private final SQLiteDatabase database;
    private final DateManager dateManager = new DateManager();
    public SleepDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void insertData(int vigilTime,
                           int lightSleepTime,
                           int deepSleepTime,
                           int remSleepTime,
                           float lightValue,
                           int loudSoundsAmount,
                           int suddenMovementsAmount,
                           int positionChangesAmount,
                           int awakeningAmount) {

        if (vigilTime < 0 || lightSleepTime < 0 || deepSleepTime < 0 || remSleepTime < 0) {
            throw new IllegalArgumentException("Invalid sleep data");
        }

        int totalSleepTime = lightSleepTime + deepSleepTime + remSleepTime;
        int timeInBed = vigilTime + totalSleepTime;
        int efficiency = Efficiency.getSleepEfficiency(timeInBed, totalSleepTime);
        String date = dateManager.getCurrentDate();

        ContentValues values = new ContentValues();
        values.put("vigilTime", vigilTime);
        values.put("lightSleepTime", lightSleepTime);
        values.put("deepSleepTime", deepSleepTime);
        values.put("REMTime", remSleepTime);
        values.put("totalSleepTime", totalSleepTime);
        values.put("efficiency", efficiency);
        values.put("loudSoundsAmount", loudSoundsAmount);

        values.put("date", date);
        values.put("lightValue", lightValue);

        values.put("awakeningAmount", awakeningAmount);
        values.put("suddenMovementsAmount", suddenMovementsAmount);
        values.put("positionChangesAmount", positionChangesAmount);

        database.insert("SleepData", null, values);
    }
}
