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

public class AudiosDataAccess {
    private final SQLiteDatabase database;
    public AudiosDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public int getAudioID(String audioTitle) {
        int audioID = -1;

        try (SQLiteStatement statement = database.compileStatement("SELECT id FROM Audios WHERE name = ?")) {
            statement.bindString(1, audioTitle);
            long result = statement.simpleQueryForLong();
            audioID = (int) result;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return audioID;
    }

    @SuppressLint("Range")
    public List<Audio> getAllAudios() {
        List<Audio> audioList = new ArrayList<>();
        String query = "SELECT * FROM Audios;";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int createdBySystem = cursor.getInt(cursor.getColumnIndex("createdBySystem"));

                Audio audio= new Audio(id, name, createdBySystem);
                audioList.add(audio);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return audioList;
    }

    @SuppressLint("Range")
    public List<String> getAudiosNotCreatedBySystem() {
        List<String> audioList = new ArrayList<>();
        String query = "SELECT name FROM Audios WHERE createdBySystem = 0;";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                audioList.add(name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return audioList;
    }
}
