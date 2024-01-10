package Database.Missions;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Database.DatabaseConnection;

public class MissionDataUpdate {
    private SQLiteDatabase database;

    public MissionDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void updateCurrentQuantity(int mission, int quantity) {
        ContentValues values = new ContentValues();
        values.put("currentCuantity", quantity);
        database.update("GameManagers", values, "id = ?", new String[] { String.valueOf(mission) });
    }

    public void updateCurrentDifficult(int mission, int difficult) {
        ContentValues values = new ContentValues();
        values.put("currentDifficult", difficult);
        database.update("GameManagers", values, "id = ?", new String[] { String.valueOf(mission) });
    }

    public void updateRequiredQuantity(int mission, int requiredQuantity) {
        ContentValues values = new ContentValues();
        values.put("requiredCuantity", requiredQuantity);
        database.update("GameManagers", values, "id = ?", new String[] { String.valueOf(mission) });
    }

    public void updateCompleteStatus(int mission) {
        ContentValues values = new ContentValues();
        values.put("completed", true);
        database.update("GameManagers", values, "id = ?", new String[] { String.valueOf(mission) });
    }

    public void updateDate(int mission) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        String newDay = dateFormat.format(date);

        ContentValues values = new ContentValues();
        values.put("date", newDay);
        database.update("GameManagers", values, "id = ?", new String[] { String.valueOf(mission) });
    }
}
