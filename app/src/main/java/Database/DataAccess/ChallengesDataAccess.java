package Database.DataAccess;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Database.DatabaseConnection;
import Dates.DateManager;

public class ChallengesDataAccess {
    private final SQLiteDatabase database;

    public ChallengesDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public boolean isChallengeAvailable(int challenge) {
        boolean available = false;
        String query = "SELECT Displayed FROM Challenges WHERE id = " + challenge + ";";

        try (Cursor cursor = database.rawQuery(query, null)) {
            if (cursor.moveToFirst()) {
                available = cursor.getInt(0) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return available;
    }

    public boolean allChallengesDisplayed() {
        boolean allDisplayed = true;
        String query = "SELECT Displayed FROM Challenges WHERE Displayed = 0;";

        try (Cursor cursor = database.rawQuery(query, null)) {
            allDisplayed = !cursor.moveToFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allDisplayed;
    }

    public boolean isCompleted(int challenge) {
        boolean completed = false;
        String query = "SELECT Completed FROM Challenges WHERE id = " + challenge + ";";

        try (Cursor cursor = database.rawQuery(query, null)) {
            if (cursor.moveToFirst()) {
                completed = cursor.getInt(0) == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return completed;
    }

    public int getActiveChallenge() {
        int challengeId = 0;
        String query = "SELECT id FROM Challenges WHERE Active = 1;";

        try (Cursor cursor = database.rawQuery(query, null)) {
            if (cursor.moveToFirst()) {
                challengeId = cursor.getInt(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return challengeId;
    }

    public String getOldDate(int challenge) {
        String date = null;
        String query = "SELECT OldDate FROM Challenges WHERE id = " + challenge + ";";

        try (Cursor cursor = database.rawQuery(query, null)) {
            if (cursor.moveToFirst()) date = cursor.getString(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }

    public String getStartDate(int challenge) {
        String date = DateManager.getCurrentDate();
        String query = "SELECT startDate FROM Challenges WHERE id = " + challenge + ";";

        try (Cursor cursor = database.rawQuery(query, null)) {
            if (cursor.moveToFirst()) date = cursor.getString(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public int getCounter(int challenge) {
        int counter = -1;
        String query = "SELECT Counter FROM Challenges WHERE id = " + challenge + ";";

        try (Cursor cursor = database.rawQuery(query, null)) {
            if (cursor.moveToFirst()) {
                counter = cursor.getInt(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return counter;
    }

    public List<Integer> getCompletedChallenges() {
        List<Integer> challenges = new ArrayList<>();
        String query = "SELECT id FROM Challenges WHERE Completed = 1;";

        try (Cursor cursor = database.rawQuery(query, null)) {
            int idColumnIndex = cursor.getColumnIndex("id");

            if (idColumnIndex != -1 && cursor.moveToFirst()) {
                do {
                    int challengeId = cursor.getInt(idColumnIndex);
                    challenges.add(challengeId);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return challenges;
    }

    public List<Integer> getFailedChallenges() {
        List<Integer> challenges = new ArrayList<>();
        String query = "SELECT id FROM Challenges WHERE Completed = 0 AND Displayed = 1 AND Active = 0;";

        try (Cursor cursor = database.rawQuery(query, null)) {
            int idColumnIndex = cursor.getColumnIndex("id");

            if (idColumnIndex != -1 && cursor.moveToFirst()) {
                do {
                    int challengeId = cursor.getInt(idColumnIndex);
                    challenges.add(challengeId);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return challenges;
    }

    public List<Integer> getUnassignedChallenges() {
        List<Integer> challenges = new ArrayList<>();
        String query = "SELECT id FROM Challenges WHERE Displayed = 0;";

        try (Cursor cursor = database.rawQuery(query, null)) {
            int idColumnIndex = cursor.getColumnIndex("id");

            if (idColumnIndex != -1 && cursor.moveToFirst()) {
                do {
                    int challengeId = cursor.getInt(idColumnIndex);
                    challenges.add(challengeId);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return challenges;
    }
}