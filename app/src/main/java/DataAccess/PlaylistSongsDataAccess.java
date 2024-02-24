package DataAccess;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import Database.DatabaseConnection;
import Music.Song;

public class PlaylistSongsDataAccess {
    private final SQLiteDatabase database;
    public PlaylistSongsDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public List<Song> getSongsFromPlaylist(int playlistID) {
        Log.d("canciones", "Playlist ID:" + playlistID);
        List<Song> songs = new ArrayList<>();
        String query = "SELECT name, id FROM PlaylistSongs INNER JOIN Songs ON PlaylistSongs.songId = Songs.id WHERE playlistId = " + playlistID;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {

                @SuppressLint("Range") int songID = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String songName = cursor.getString(cursor.getColumnIndex("name"));
                Log.d("canciones", "Song ID:" + songID);
                Log.d("canciones", "Song Name:" + songName);

                Song song = new Song(songID, songName, 0);
                songs.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return songs;
    }

    public List<Song> getNotSongsFromPlaylist(int playlistID) {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT name, id FROM Songs WHERE id NOT IN (SELECT songId FROM PlaylistSongs WHERE playlistId = " + playlistID + ")";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int songID = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String songName = cursor.getString(cursor.getColumnIndex("name"));

                Song song = new Song(songID, songName, 0);
                songs.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return songs;
    }
}
