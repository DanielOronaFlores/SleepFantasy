package DataAccess;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import Database.DatabaseConnection;

public class MonstersDataAccess {
    private final SQLiteDatabase database;
    public MonstersDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public int getActiveMonster() {
        String query = "SELECT id FROM Monsters WHERE active = 1;";
        SQLiteStatement statement = database.compileStatement(query);
        long result = statement.simpleQueryForLong();


        if (result != 0) {
            return (int) result;
        } else {
            return -1;
        }
    }

    public String getDateAppearedActiveMonster() {
        String query = "SELECT dateAppeared FROM Monsters WHERE active = 1;";
        SQLiteStatement statement = database.compileStatement(query);
        return statement.simpleQueryForString();
    }
}
