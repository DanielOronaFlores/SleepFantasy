package Database.DataUpdates;

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

    public void deletePlaylist(int playlistID) {
        database.execSQL("DELETE FROM Playlist WHERE id = " + playlistID);
    }

    public void updatePlaylist(int playlistID, String name) {
        database.execSQL("UPDATE Playlist SET name = '" + name + "' WHERE id = " + playlistID);
    }
}
