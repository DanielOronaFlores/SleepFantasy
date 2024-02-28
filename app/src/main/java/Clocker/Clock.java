package Clocker;

import android.annotation.SuppressLint;

import AppContext.MyApplication;
import DataAccess.PreferencesDataAccess;
import Database.DatabaseConnection;

public class Clock {
    private final PreferencesDataAccess preferencesDataAccess;

    public Clock() {
        DatabaseConnection connection = DatabaseConnection.getInstance(MyApplication.getAppContext());
        preferencesDataAccess = new PreferencesDataAccess(connection);
    }

    @SuppressLint("DefaultLocale")
    public String getTimeString() {
        long millis = preferencesDataAccess.getTimerDuration();

        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        long remainingMinutes = minutes % 60;

        String formattedHours = String.format("%02d", hours);
        String formattedMinutes = String.format("%02d", remainingMinutes);

        return formattedHours + ":" + formattedMinutes;
    }

    public int convertMillisToHours(long millis) {
        return (int) (millis / (1000 * 60 * 60));
    }
    public int convertMillisToMinutes(long millis) {
        return (int) (millis / (1000 * 60)) % 60;
    }
}
