package Database.DataAccess;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import Database.DatabaseConnection;
import Models.Mission;

public class MissionDataAccess {
    private final SQLiteDatabase database;
    public MissionDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public int getCurrentDifficult(int mission) {
        String query = "SELECT currentDifficult FROM Missions WHERE id = ?;";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindLong(1, mission);
        return (int) statement.simpleQueryForLong();
    }

    public int getRequiredQuantity(int mission) {
        String query = "SELECT requiredCuantity FROM Missions WHERE id = ?;";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindLong(1, mission);
        return (int) statement.simpleQueryForLong();
    }

    public String getDate(int mission) {
        String query = "SELECT date FROM Missions WHERE id = ?;";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindLong(1, mission);
        return statement.simpleQueryForString();
    }

    @SuppressLint("Range")
    public List<Mission> getAllMissions() {
        String query = "SELECT * FROM Missions;";
        List<Mission> missions = new ArrayList<>();

        try (Cursor cursor = database.rawQuery(query, null)) {
            while (cursor.moveToNext()) {
               byte id = (byte) cursor.getInt(cursor.getColumnIndex("id"));
                byte currentDifficult = (byte) cursor.getInt(cursor.getColumnIndex("currentDifficult"));
                byte currentQuantity = (byte) cursor.getInt(cursor.getColumnIndex("currentCuantity"));
                byte requiredQuantity = (byte) cursor.getInt(cursor.getColumnIndex("requiredCuantity"));
                String date = String.valueOf(cursor.getInt(cursor.getColumnIndex("date")));
                boolean completed = cursor.getInt(cursor.getColumnIndex("completed")) == 1;

                missions.add(new Mission(id, currentDifficult, currentQuantity, requiredQuantity, date, completed));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return missions;
    }

    //TODO: Vamos a actualizar el reporte de codigo.
    public int getCurrentQuantity(int mission) {
        String query = "SELECT currentCuantity FROM Missions WHERE id = ?;";
        SQLiteStatement statement = database.compileStatement(query);
        statement.bindLong(1, mission);
        return (int) statement.simpleQueryForLong();
    }
}
