package Database.Missions;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class MissionDataUpdate {
    private SQLiteDatabase database;

    public MissionDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void updateCurrentQuantity(int mission, int quantity) {
        ContentValues values = new ContentValues();
        values.put("currentCuantity", quantity);
        database.update("Missions", values, "id = ?", new String[] { String.valueOf(mission) });
    }

    public void updateCurrentDifficult(int mission, int difficult) {
        ContentValues values = new ContentValues();
        values.put("currentDifficult", difficult);
        database.update("Missions", values, "id = ?", new String[] { String.valueOf(mission) });
    }

    public void updateRequiredQuantity(int mission, int requiredQuantity) {
        ContentValues values = new ContentValues();
        values.put("requiredCuantity", requiredQuantity);
        database.update("Missions", values, "id = ?", new String[] { String.valueOf(mission) });
    }

    public void updateCompleteStatus(int mission) {
        ContentValues values = new ContentValues();
        values.put("completed", true);
        database.update("Missions", values, "id = ?", new String[] { String.valueOf(mission) });
    }
}
