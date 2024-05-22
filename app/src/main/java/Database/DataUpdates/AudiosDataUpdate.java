package Database.DataUpdates;

import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class AudiosDataUpdate {
    private final SQLiteDatabase database;

    public AudiosDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void addAudio(String audioName, boolean createdBySystem) {
        int createdBySystemInt = createdBySystem ? 1 : 0;
        String query = "INSERT INTO Audios (name, createdBySystem) VALUES (?, ?)";
        database.execSQL(query, new String[]{audioName, String.valueOf(createdBySystemInt)});
    }

    public void deleteAudio(String audioName) {
        String query = "DELETE FROM Audios WHERE name = ?";
        database.execSQL(query, new String[]{audioName});
    }
}
