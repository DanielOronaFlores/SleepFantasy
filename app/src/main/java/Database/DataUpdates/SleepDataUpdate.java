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
                           int suddenMovementsAmount,
                           int positionChangesAmount,
                           int awakeningAmount) {

        int totalSleepTime = lightSleepTime + deepSleepTime + remSleepTime;
        int efficiency = Efficiency.getSleepEfficiency(totalSleepTime, vigilTime);
        String date = dateManager.getCurrentDate();

        ContentValues values = new ContentValues();
        values.put("vigilTime", vigilTime);
        values.put("lightSleepTime", lightSleepTime);
        values.put("deepSleepTime", deepSleepTime);
        values.put("REMTime", remSleepTime);
        values.put("totalSleepTime", totalSleepTime);
        values.put("efficiency", efficiency);

        values.put("date", date);
        values.put("lightValue", lightValue);

        values.put("awakeningAmount", awakeningAmount);
        values.put("suddenMovementsAmount", suddenMovementsAmount);
        values.put("positionChangesAmount", positionChangesAmount);

        database.insert("SleepData", null, values);
    }
}
