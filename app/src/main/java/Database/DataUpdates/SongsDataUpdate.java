package Database.DataUpdates;

import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class SongsDataUpdate {
    private final SQLiteDatabase database;

    public SongsDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void addSong(String songName, boolean createdBySystem) {
        int ibBySystem = createdBySystem ? 1 : 0;
        String query = "INSERT INTO Songs (name, ibBySystem) VALUES (?, ?)";
        database.execSQL(query, new String[]{songName, String.valueOf(ibBySystem)});
    }

    public void deleteSong(String songName) {
        String query = "DELETE FROM Songs WHERE name = ?";
        database.execSQL(query, new String[]{songName});
    }
}
