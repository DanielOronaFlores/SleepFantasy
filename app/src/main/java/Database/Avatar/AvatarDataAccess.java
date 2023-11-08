package Database.Avatar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class AvatarDataAccess {
    private SQLiteDatabase database;

    public AvatarDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public String getAvatarName() {
        String query = "SELECT name FROM Avatar LIMIT 1;";
        Cursor cursor = database.rawQuery(query, null);
        String name = "";
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        cursor.close();
        return name;
    }
    public String getAvatarAge() {
        String query = "SELECT age FROM Avatar LIMIT 1;";
        Cursor cursor = database.rawQuery(query, null);
        String age = "";
        if (cursor.moveToFirst()) {
            age = cursor.getString(0);
        }
        cursor.close();
        return age;
    }

    public byte getLevel() {
        String query = "SELECT level FROM Avatar LIMIT 1;";
        Cursor cursor = database.rawQuery(query, null);
        byte level = 0;
        if (cursor.moveToFirst()) {
            level = (byte) cursor.getInt(0);
        }
        cursor.close();
        return level;
    }

    public byte getCharacterClass() {
        String query = "SELECT characterClass FROM Avatar LIMIT 1;";
        Cursor cursor = database.rawQuery(query, null);
        byte characterClass = 0;
        if (cursor.moveToFirst()) {
            characterClass = (byte) cursor.getInt(0);
        }
        cursor.close();
        return characterClass;
    }

    public byte getCharacterID() {
        String query = "SELECT characterClass FROM Avatar LIMIT 1;";
        Cursor cursor = database.rawQuery(query, null);
        byte characterID = 0;
        if (cursor.moveToFirst()) {
            characterID = (byte) cursor.getInt(0);
        }
        cursor.close();
        return characterID;
    }

    public byte getCharacterPhase() {
        String query = "SELECT characterPhase FROM Avatar LIMIT 1;";
        Cursor cursor = database.rawQuery(query, null);
        byte characterPhase = 0;
        if (cursor.moveToFirst()) {
            characterPhase = (byte) cursor.getInt(0);
        }
        cursor.close();
        return characterPhase;
    }

    public int getCurrentExperience() {
        String query = "SELECT currentExperience FROM Avatar LIMIT 1;";
        Cursor cursor = database.rawQuery(query, null);
        int currentExperience = 0;
        if (cursor.moveToFirst()) {
            currentExperience = cursor.getInt(0);
        }
        cursor.close();
        return currentExperience;
    }

    public int getRequiredExperience() {
        String query = "SELECT requiredExperience FROM Avatar LIMIT 1;";
        Cursor cursor = database.rawQuery(query, null);
        int requiredExperience = 0;
        if (cursor.moveToFirst()) {
            requiredExperience = cursor.getInt(0);
        }
        cursor.close();
        return requiredExperience;
    }
}