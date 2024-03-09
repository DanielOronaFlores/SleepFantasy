package Database.DataUpdates;

import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class MonstersDataUpdate {
    private final SQLiteDatabase database;
    public MonstersDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void updateMonsterActiveStatus(int id, String date) {
        String query = "UPDATE Monster SET active = 1, date = '" + date + "' WHERE id = " + id + ";";
        database.execSQL(query);
    }

    public void updateMonsterInactiveStatus(int id) {
        String query = "UPDATE Monster SET active = 0 WHERE id = " + id + ";";
        database.execSQL(query);
    }

    public void updateMonsterOldDate(int id, String date) {
        String query = "UPDATE Monster SET oldDate = '" + date + "' WHERE id = " + id + ";";
        database.execSQL(query);
    }
}
