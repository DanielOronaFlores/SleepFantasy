package Database.DataAccess;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import Database.DatabaseConnection;
import Models.Playlist;

public class PlaylistDataAccess {
    private final SQLiteDatabase database;
    public PlaylistDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public boolean isPlaylistCreated(String playlistName) {
        boolean isCreated = false;
        String query = "SELECT * FROM Playlist WHERE name = ?";


        try (Cursor cursor = database.rawQuery(query, new String[]{playlistName})) {
            isCreated = cursor.getCount() > 0;
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return isCreated;
    }

    public String getPlaylistTitle(int playlistID) {
        String query = "SELECT name FROM PlayList WHERE id = " + playlistID + ";";
        return database.compileStatement(query).simpleQueryForString();
    }
    @SuppressLint("Range")
    public int getPlaylistId(String playlistName) {
        int playlistID = -1;
        String query = "SELECT id FROM Playlist WHERE name = ?";

        try (Cursor cursor = database.rawQuery(query, new String[]{playlistName})) {
            if (cursor.moveToFirst()) {
                playlistID = cursor.getInt(cursor.getColumnIndex("id"));
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return playlistID;
    }
    public boolean isCreatedBySystem(int playlistID) {
        String query = "SELECT createdBySystem FROM Playlist WHERE id = " + playlistID + ";";
        return database.compileStatement(query).simpleQueryForLong() == 1;
    }

    @SuppressLint("Range")
    public List<Playlist> getAllPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        String query = "SELECT * FROM Playlist;";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int createdBySystem = cursor.getInt(cursor.getColumnIndex("createdBySystem"));

                Playlist playlist = new Playlist(id, name, createdBySystem);
                playlists.add(playlist);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return playlists;
    }
}
