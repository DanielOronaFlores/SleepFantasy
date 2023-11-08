package Database.Missions;

import android.database.sqlite.SQLiteDatabase;

public class MissionDataAccess {
    private SQLiteDatabase database;
    public MissionDataAccess(SQLiteDatabase database) {
        this.database = database;
    }

    public int getCurrentDifficult() {
        String query = "SELECT currentDifficult FROM Mission"; //TODO: add where clause
    }
}
