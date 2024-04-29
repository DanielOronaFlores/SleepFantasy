package Database.DataUpdates;

import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class PlaylistAudiosDataUpdate {
    private final String TABLE_NAME = "PlaylistAudios";
    private final SQLiteDatabase database;
    public PlaylistAudiosDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void addaudioToPlaylist(int playlistId, int audioId) {
        database.execSQL("INSERT INTO PlaylistAudios (playlistId, audioId) VALUES (" + playlistId + ", " + audioId + ")");
    }

    public void deleteAudioFromPlaylist(int playlistId, int audioId) {
        database.execSQL("DELETE FROM PlaylistAudios WHERE playlistId = " + playlistId + " AND audioId = " + audioId);
    }

    public void deleteAudio(int audioID) {
        database.delete(TABLE_NAME, "audioId=?", new String[]{String.valueOf(audioID)});
    }
}
