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
        String query = "SELECT currentDifficult FROM Missions WHERE id = ?;";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindLong(1, mission);
        return (int) statement.simpleQueryForLong();
    }

    public int getRequiredQuantity(int mission) {
        String query = "SELECT requiredCuantity FROM Missions WHERE id = ?;";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindLong(1, mission);
        return (int) statement.simpleQueryForLong();
    }

    public int getCurrentQuantity(int mission) {
        String query = "SELECT currentCuantity FROM Missions WHERE id = ?;";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindLong(1, mission);
        return (int) statement.simpleQueryForLong();
    }
}
