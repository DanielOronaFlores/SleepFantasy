package DataAccess;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import Database.DatabaseConnection;

public class PlaylistDataAccess {
    private SQLiteDatabase database;
    public PlaylistDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public boolean isPlaylistCreated(String playlistName) {
        String query = "SELECT * FROM Playlist WHERE name = ?";

        try (Cursor cursor = database.rawQuery(query, new String[]{playlistName})) {
            return cursor.getCount() > 0;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressLint("Range")
    public int getPlaylistId(String playlistName) {
        String query = "SELECT id FROM Playlist WHERE name = ?";

        try (Cursor cursor = database.rawQuery(query, new String[]{playlistName})) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex("id"));
            } else {
                return -1;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
