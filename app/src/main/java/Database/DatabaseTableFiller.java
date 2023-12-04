package Database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseTableFiller {
    public static void fillTables(SQLiteDatabase db) {
        fillMissionTable(db);
    }
    private static void fillMissionTable(SQLiteDatabase db) {
        for (int i = 0; i < 20; i++) {
            ContentValues values = new ContentValues();
            values.put("currentDifficult", 1);
            values.put("currentCuantity", 0);

            if (i == 6 || i == 18 ) {
                values.put("requiredCuantity", 10);
            } else if (i == 12) {
                values.put("requiredCuantity", 1);
            } else {
                values.put("requiredCuantity", 3);
            }

            String currentDate = getCurrentDate();
            values.put("date", currentDate);

            values.put("completed", false);
            db.insert("Missions", null, values);
        }
    }

    private static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }
}
