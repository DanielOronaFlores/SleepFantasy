package Database.DataUpdates;

import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class ChallengesDataUpdate {
    private final SQLiteDatabase database;

    public ChallengesDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void markAsDisplayed(int challenge) {
        String query = "UPDATE Challenges SET Displayed = 1 WHERE id = " + challenge + ";";
        database.execSQL(query);
    }

    public void markAsCompleted(int challenge) {
        String query = "UPDATE Challenges SET Completed = 1 WHERE id = " + challenge + ";";
        database.execSQL(query);
    }

    public void markAsActive(int challenge) {
        String query = "UPDATE Challenges SET Active = 1 WHERE id = " + challenge + ";";
        database.execSQL(query);
    }

    public void markAsInactive(int challenge) {
        String query = "UPDATE Challenges SET Active = 0 WHERE id = " + challenge + ";";
        database.execSQL(query);
    }

    public void resetChallenges() {
        String query = "UPDATE Challenges SET Displayed = 0, Completed = 0, Active = 0, Counter = 0;";
        database.execSQL(query);
    }

    public void updateCounter(int challenge, int quantity) {
        String query = "UPDATE Challenges SET Counter = " + quantity + " WHERE id = " + challenge + ";";
        database.execSQL(query);
    }

    public void updateOldDate(int challenge, String currentDate) {
        String query = "UPDATE Challenges SET OldDate = '" + currentDate + "' WHERE id = " + challenge + ";";
        database.execSQL(query);
    }

    public void updateStartDate(int challenge, String date) {
        String query = "UPDATE Challenges SET startDate = '" + date + "' WHERE id = " + challenge + ";";
        database.execSQL(query);
    }
}
