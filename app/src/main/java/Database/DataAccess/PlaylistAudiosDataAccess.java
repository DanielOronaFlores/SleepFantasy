package Database.DataAccess;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Database.DatabaseConnection;
import Models.Audio;

public class PlaylistAudiosDataAccess {
    private final SQLiteDatabase database;
    public PlaylistAudiosDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    @SuppressLint("Range")
    public List<Audio> getAudiosFromPlaylist(int playlistID) {
        String query = "SELECT name, id, createdBySystem FROM PlaylistAudios INNER JOIN Audios ON PlaylistAudios.audioId = Audios.id WHERE playlistId = " + playlistID;
        Cursor cursor = database.rawQuery(query, null);

        List<Audio> audios = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int audioID = cursor.getInt(cursor.getColumnIndex("id"));
                String audioName = cursor.getString(cursor.getColumnIndex("name"));
                int isCreatedBySystem = cursor.getInt(cursor.getColumnIndex("createdBySystem"));

                Audio audio = new Audio(audioID, audioName, isCreatedBySystem);
                audios.add(audio);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return audios;
    }

    @SuppressLint("Range")
    public List<Audio> getNotAudiosFromPlaylist(int playlistID) {
        String query = "SELECT name, id, createdBySystem FROM Audios WHERE id NOT IN (SELECT audioId FROM PlaylistAudios WHERE playlistId = " + playlistID + ")";

        Cursor cursor = database.rawQuery(query, null);
        List<Audio> audios = new ArrayList<>();
        
        if (cursor.moveToFirst()) {
            do {
                int audioID = cursor.getInt(cursor.getColumnIndex("id"));
                String audioName = cursor.getString(cursor.getColumnIndex("name"));
                int isCreatedBySystem = cursor.getInt(cursor.getColumnIndex("createdBySystem"));

                Audio audio= new Audio(audioID, audioName, isCreatedBySystem);
                audios.add(audio);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return audios;
    }
}
