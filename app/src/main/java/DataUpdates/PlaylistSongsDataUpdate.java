package DataUpdates;

import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class PlaylistSongsDataUpdate {
    private final SQLiteDatabase database;
    public PlaylistSongsDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void addSongToPlaylist(int playlistId, int songId) {
        database.execSQL("INSERT INTO PlaylistSongs (playlistId, songId) VALUES (" + playlistId + ", " + songId + ")");
    }

    public void deleteSongFromPlaylist(int playlistId, int songId) {
        database.execSQL("DELETE FROM PlaylistSongs WHERE playlistId = " + playlistId + " AND songId = " + songId);
    }
}
