package Database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseTableFiller {
    public static void fillTables(SQLiteDatabase db) {
        fillMissionTable(db);
        fillChallengeTable(db);
        fillRecordTable(db);
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
            db.insert("GameManagers", null, values);
        }
    }

    private static void fillChallengeTable(SQLiteDatabase db) {
        for (int i = 0; i < 15; i++) {
            ContentValues values = new ContentValues();
            values.put("Displayed", false);
            values.put("Completed", false);
            values.put("Counter", 0);
            values.put("Active", false);
            db.insert("Challenges", null, values);
        }
    }

    //Fill full table with false values
    public static void fillRecordTable(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("isPlayingMusic", false);
        values.put("isTemporizerActive", false);
        values.put("hasMonsterAppeared", false);
        values.put("isCategoryValid", false);
        values.put("hasDeletedAudio", false);
        values.put("hasAvatarVisualChanged", false);
        values.put("isNewSoundSet", false);
        values.put("isNewInterface", false);
        values.put("isNewAudioUpoloaded", false);
        values.put("hasAudiosPlayed", false);
        values.put("isGraphDisplayed", false);
        values.put("hasObtainedExperience", false);
        db.insert("Records", null, values);
    }

    private static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }
}
