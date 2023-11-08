package Database.Avatar;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import Database.DatabaseConnection;

public class AvatarDataUpdate {
    private SQLiteDatabase database;

    public AvatarDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void updateExperience(int experience) {
        Log.d("MiApp", "Se llama a updateExperience.");
        ContentValues values = new ContentValues();
        values.put("currentExperience", experience);
        database.update("Avatar", values, null, null);
        Log.d("Tag", "Se actualiza la experiencia.");
    }

    public void updateRequiredExperience(int requiredExperience) {
        ContentValues values = new ContentValues();
        values.put("requiredExperience", requiredExperience);
        database.update("Avatar", values, null, null);
    }

    public void updateLevel(int currenteLevel) {
        ContentValues values = new ContentValues();
        values.put("level", currenteLevel);
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
