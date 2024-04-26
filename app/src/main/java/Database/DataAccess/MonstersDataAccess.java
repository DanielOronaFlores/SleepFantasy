package Database.DataAccess;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import Database.DatabaseConnection;

public class MonstersDataAccess {
    private final SQLiteDatabase database;
    public MonstersDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public int getActiveMonster() {
        int activeMonster = -1;
        String query = "SELECT id FROM Monster WHERE active = 1;";

        try {
            SQLiteStatement statement = database.compileStatement(query);
            activeMonster = (int) statement.simpleQueryForLong();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return activeMonster;
    }

    public String getDateAppearedActiveMonster() {
        String query = "SELECT dateAppeared FROM Monster WHERE active = 1;";
        SQLiteStatement statement = database.compileStatement(query);
        return statement.simpleQueryForString();
    }
}
