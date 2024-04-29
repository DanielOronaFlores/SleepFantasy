package Database.DataUpdates;

import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class AudiosDataUpdate {
    private final SQLiteDatabase database;

    public AudiosDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void addAudio(String audioName, boolean createdBySystem) {
        String query = "INSERT INTO Audios (name, createdBySystem) VALUES (?, ?)";
        database.execSQL(query, new String[]{audioName, String.valueOf(createdBySystem)});
    }

    public void deleteAudio(String audioName) {
        String query = "DELETE FROM Audios WHERE name = ?";
        database.execSQL(query, new String[]{audioName});
    }
}
