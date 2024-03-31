package Database.DataUpdates;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class AvatarCreator {
    private final SQLiteDatabase database;

    public AvatarCreator(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void createAvatar(String name, byte age, byte level, byte characterClass, int currentExperience, int requiredExperience, byte characterPhase) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("age", age);
        values.put("level", level);
        values.put("characterClass", characterClass);
        values.put("currentExperience", currentExperience);
        values.put("requiredExperience", requiredExperience);
        values.put("characterPhase", characterPhase);
        database.insert("Avatar", null, values);
    }

    public boolean isAvatarCreated() {
        String query = "SELECT 1 FROM Avatar LIMIT 1;";
        Cursor cursor = database.rawQuery(query, null);
        boolean avatarExists = cursor.getCount() > 0;
        cursor.close();
        return avatarExists;
    }
}