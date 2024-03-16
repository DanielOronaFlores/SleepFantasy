package Database.DataAccess;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import Database.DatabaseConnection;
import Models.Song;

public class SongsDataAccess {
    private final SQLiteDatabase database;
    public SongsDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public String getSongTitle(int songID) {
        String query = "SELECT name FROM Songs WHERE id = " + songID + ";";
        return database.compileStatement(query).simpleQueryForString();
    }

    public List<Song> getAllSongs() {
        List<Song> songList = new ArrayList<>();
        String query = "SELECT * FROM Songs;";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // Obtener los valores de cada columna
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") int ibBySystem = cursor.getInt(cursor.getColumnIndex("ibBySystem"));

                // Crear un objeto Song con los valores obtenidos y agregarlo a la lista
                Song song = new Song(id, name, ibBySystem);
                songList.add(song);
                Log.d("Song", "New song added: " + song.getName());
            } while (cursor.moveToNext());
        }
        cursor.close();
        return songList;
    }
}
