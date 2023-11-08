package Database.Missions;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import Database.DatabaseConnection;

public class MissionDataAccess {
    private final SQLiteDatabase database;
    public MissionDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public int getCurrentDifficult(int mission) {
        String query = "SELECT currentDifficult FROM MissionsDifficult WHERE id = ?;";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindLong(1, mission);
        return (int) statement.simpleQueryForLong();
    }
}
