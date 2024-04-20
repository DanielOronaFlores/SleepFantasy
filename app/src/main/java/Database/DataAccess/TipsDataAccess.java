package Database.DataAccess;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteStatement;

import Database.DatabaseConnection;

public class TipsDataAccess {
    private final SQLiteDatabase database;
    public TipsDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public int getCurrentTipType() {
        String query = "SELECT type FROM Tips WHERE current = 1;";
        SQLiteStatement statement = database.compileStatement(query);
        try {
            long result = statement.simpleQueryForLong();
            return (int) result;
        } catch (SQLiteDoneException e) {
            return -1;
        }
    }

    public String getLastDateAppeared() {
        String query = "SELECT lastDateAppeared FROM Tips WHERE current = 1;";
        SQLiteStatement statement = database.compileStatement(query);
        try {
            return statement.simpleQueryForString();
        } catch (SQLiteDoneException e) {
            return null;
        }
    }
}
