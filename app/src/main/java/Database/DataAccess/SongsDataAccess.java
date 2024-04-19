package Database.DataAccess;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import Database.DatabaseConnection;
import Models.Audio;

public class SongsDataAccess {
    private final SQLiteDatabase database;
    public SongsDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public String getSongTitle(int songID) {
        String query = "SELECT name FROM Songs WHERE id = " + songID + ";";
        return database.compileStatement(query).simpleQueryForString();
    }

    public int getSongID(String songTitle) {
        int songID = -1;

        try (SQLiteStatement statement = database.compileStatement("SELECT id FROM Songs WHERE name = ?")) {
            statement.bindString(1, songTitle);
            long result = statement.simpleQueryForLong();
            songID = (int) result;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return songID;
    }

    public List<Audio> getAllSongs() {
        List<Audio> songList = new ArrayList<>();
        String query = "SELECT * FROM Songs;";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") int ibBySystem = cursor.getInt(cursor.getColumnIndex("ibBySystem"));

                Audio song = new Audio(id, name, ibBySystem);
                songList.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return songList;
    }

    public List<String> getAudiosNotCreatedBySystem() {
        List<String> songList = new ArrayList<>();
        String query = "SELECT name FROM Songs WHERE ibBySystem = 0;";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                songList.add(name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return songList;
    }
}
