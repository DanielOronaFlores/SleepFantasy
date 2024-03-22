package Database.DataUpdates;

import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class PlaylistSongsDataUpdate {
    private final String TABLE_NAME = "PlaylistSongs";
    private final SQLiteDatabase database;
    public PlaylistSongsDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void addSongToPlaylist(int playlistId, int songId) {
        database.execSQL("INSERT INTO PlaylistSongs (playlistId, songId) VALUES (" + playlistId + ", " + songId + ")");
    }

    public void deleteAudioFromPlaylist(int playlistId, int songId) {
        database.execSQL("DELETE FROM PlaylistSongs WHERE playlistId = " + playlistId + " AND songId = " + songId);
    }

    public void deleteAudio(int audioID) {
        database.delete(TABLE_NAME, "songId=?", new String[]{String.valueOf(audioID)});
    }
}
