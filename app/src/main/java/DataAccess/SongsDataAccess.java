package DataAccess;

import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class SongsDataAccess {
    private SQLiteDatabase database;
    public SongsDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public String getSongTitle(int songID) {
        String query = "SELECT name FROM Songs WHERE id = " + songID + ";";
        return database.compileStatement(query).simpleQueryForString();
    }
}
