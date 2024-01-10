package Database.Challenges;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import Database.DatabaseConnection;

public class ChallengesDataUpdate {
    private SQLiteDatabase database;

    public ChallengesDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void markAsDisplayed(int challenge) {
        Log.d("UPDATE", "Displayed inicio");
        String query = "UPDATE Challenges SET Displayed = 1 WHERE id = " + challenge + ";";
        database.execSQL(query);
        Log.d("UPDATE", "Displayed fin");
    }

    public void markAsCompleted(int challenge) {
        String query = "UPDATE Challenges SET Completed = 1 WHERE id = " + challenge + ";";
        database.execSQL(query);
    }

    public void markAsActive(int challenge) {
        Log.d("UPDATE", "Active inicio");
        String query = "UPDATE Challenges SET Active = 1 WHERE id = " + challenge + ";";
        database.execSQL(query);
        Log.d("UPDATE", "Active fin");
    }

    public void resetChallenges() {
        String query = "UPDATE Challenges SET Displayed = 0, Completed = 0, Active = 0, Counter = 0;";
        database.execSQL(query);
    }
}
