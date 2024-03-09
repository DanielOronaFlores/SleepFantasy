package Database.DataUpdates;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class AvatarDataUpdate {
    private final SQLiteDatabase database;

    public AvatarDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void updateExperience(int experience) {
        ContentValues values = new ContentValues();
        values.put("currentExperience", experience);
        database.update("Avatar", values, null, null);
    }

    public void updateRequiredExperience(int requiredExperience) {
        ContentValues values = new ContentValues();
        values.put("requiredExperience", requiredExperience);
        database.update("Avatar", values, null, null);
    }

    public void updateLevel(int currentLevel) {
        ContentValues values = new ContentValues();
        values.put("level", currentLevel);
        database.update("Avatar", values, null, null);
    }

    public void updateNameAndAge(String name, int age) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("age", age);
        database.update("Avatar", values, null, null);
    }

    public void updateCharacterPhase(byte characterPhase) {
        ContentValues values = new ContentValues();
        values.put("characterPhase", characterPhase);
        database.update("Avatar", values, null, null);
    }
}
