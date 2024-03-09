package Database.DataAccess;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import Database.DatabaseConnection;

public class AvatarDataAccess {
    private final SQLiteDatabase database;

    public AvatarDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public String getAvatarName() {
        String name = null;

        try (Cursor cursor = database.rawQuery("SELECT name FROM Avatar LIMIT 1;", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                name = cursor.getString(0);
            }
        } catch (SQLiteException e) {
            // Manejar la excepción, por ejemplo, registrándola o lanzando una excepción personalizada.
            e.printStackTrace();
        }
        return name;
    }

    public String getAvatarAge() {
        String age = null;

        try (Cursor cursor = database.rawQuery("SELECT age FROM Avatar LIMIT 1;", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                age = cursor.getString(0);
            }
        } catch (SQLiteException e) {
            // Manejar la excepción, por ejemplo, registrándola o lanzando una excepción personalizada.
            e.printStackTrace();
        }
        return age;
    }

    public byte getLevel() {
        byte level = -1;

        try (Cursor cursor = database.rawQuery("SELECT level FROM Avatar LIMIT 1;", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                level = (byte) cursor.getInt(0);
            }
        } catch (SQLiteException e) {
            // Manejar la excepción, por ejemplo, registrándola o lanzando una excepción personalizada.
            e.printStackTrace();
        }
        return level;
    }

    public byte getCharacterClass() {
        byte characterClass = -1;

        try (Cursor cursor = database.rawQuery("SELECT characterClass FROM Avatar LIMIT 1;", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                characterClass = (byte) cursor.getInt(0);
            }
        } catch (SQLiteException e) {
            // Manejar la excepción, por ejemplo, registrándola o lanzando una excepción personalizada.
            e.printStackTrace();
        }
        return characterClass;
    }

    public byte getCharacterPhase() {
        byte characterPhase = -1;

        try (Cursor cursor = database.rawQuery("SELECT characterPhase FROM Avatar LIMIT 1;", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                characterPhase = (byte) cursor.getInt(0);
            }
        } catch (SQLiteException e) {
            // Manejar la excepción, por ejemplo, registrándola o lanzando una excepción personalizada.
            e.printStackTrace();
        }
        return characterPhase;
    }

    public int getCurrentExperience() {
        int currentExperience = -1;

        try (Cursor cursor = database.rawQuery("SELECT currentExperience FROM Avatar LIMIT 1;", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                currentExperience = cursor.getInt(0);
            }
        } catch (SQLiteException e) {
            // Manejar la excepción, por ejemplo, registrándola o lanzando una excepción personalizada.
            e.printStackTrace();
        }
        return currentExperience;
    }

    public int getRequiredExperience() {
        int requiredExperience = -1;

        try (Cursor cursor = database.rawQuery("SELECT requiredExperience FROM Avatar LIMIT 1;", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                requiredExperience = cursor.getInt(0);
            }
        } catch (SQLiteException e) {
            // Manejar la excepción, por ejemplo, registrándola o lanzando una excepción personalizada.
            e.printStackTrace();
        }
        return requiredExperience;
    }
}