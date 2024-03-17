package Database.DataUpdates;

import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class SongsDataUpdate {
    private final SQLiteDatabase database;

    public SongsDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void addSong(String songName) {
        String query = "INSERT INTO Songs (name, ibBySystem) VALUES (?, ?)";
        database.execSQL(query, new String[]{songName, "0"});
    }
}
