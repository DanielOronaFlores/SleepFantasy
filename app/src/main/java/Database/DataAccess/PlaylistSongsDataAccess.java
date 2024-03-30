package Database.DataAccess;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Database.DatabaseConnection;
import Models.Audio;

public class PlaylistSongsDataAccess {
    private final SQLiteDatabase database;
    public PlaylistSongsDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    @SuppressLint("Range")
    public List<Audio> getSongsFromPlaylist(int playlistID) {
        List<Audio> songs = new ArrayList<>();
        String query = "SELECT name, id, ibBySystem FROM PlaylistSongs INNER JOIN Songs ON PlaylistSongs.songId = Songs.id WHERE playlistId = " + playlistID;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {

                int songID = cursor.getInt(cursor.getColumnIndex("id"));
                String songName = cursor.getString(cursor.getColumnIndex("name"));
                int isCreatedBySystem = cursor.getInt(cursor.getColumnIndex("ibBySystem"));

                Audio song = new Audio(songID, songName, isCreatedBySystem);
                songs.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return songs;
    }

    @SuppressLint("Range")
    public List<Audio> getNotSongsFromPlaylist(int playlistID) {
        List<Audio> songs = new ArrayList<>();
        String query = "SELECT name, id, ibBySystem FROM Songs WHERE id NOT IN (SELECT songId FROM PlaylistSongs WHERE playlistId = " + playlistID + ")";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int songID = cursor.getInt(cursor.getColumnIndex("id"));
                String songName = cursor.getString(cursor.getColumnIndex("name"));
                int isCreatedBySystem = cursor.getInt(cursor.getColumnIndex("ibBySystem"));

                Audio song = new Audio(songID, songName, isCreatedBySystem);
                songs.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return songs;
    }
}
