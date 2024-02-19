package DataAccess;

import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class PlayListsDataAccess {
    private SQLiteDatabase database;
    public PlayListsDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public String getPlaylistTitle(int playlistID) {
        String query = "SELECT name FROM PlayList WHERE id = " + playlistID + ";";
        return database.compileStatement(query).simpleQueryForString();
    }
}
