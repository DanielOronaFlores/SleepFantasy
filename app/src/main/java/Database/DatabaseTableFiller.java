package Database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseTableFiller {
    public static void fillTables(SQLiteDatabase db) {
        fillMissionTable(db);
    }
    private static void fillMissionTable(SQLiteDatabase db) {
        for (int i = 0; i < 20; i++) {
            ContentValues values = new ContentValues();
            values.put("currentDifficult", 1);

            db.insert("MissionsDifficult", null, values);
        }
    }
}
