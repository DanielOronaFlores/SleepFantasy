package DataUpdates;

import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class PlaylistDataUpdate {
    private final SQLiteDatabase database;
    public PlaylistDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void createPlaylist(String playlistName) {
        database.execSQL("INSERT INTO Playlist (name, createdBySystem) VALUES ('" + playlistName + "', 0)");
    }
}
