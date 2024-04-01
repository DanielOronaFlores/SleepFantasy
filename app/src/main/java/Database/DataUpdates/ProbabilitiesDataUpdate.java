package Database.DataUpdates;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class ProbabilitiesDataUpdate {
    private final String TABLE_NAME = "Probabilities";
    private final SQLiteDatabase database;
    public ProbabilitiesDataUpdate(DatabaseConnection connection) {
        this.database = connection.getDatabase();
    }

    public void addProbabilities(int attribute, float[][] probabilities) {
        String attributeName = getAttributeName(attribute);

        ContentValues values = new ContentValues();
        values.put("attributeName", attributeName);

        for (int i = 0; i < probabilities.length; i++) {
            values.put("range", i + 1);
            for (int j = 0; j < probabilities[i].length; j++) {
                values.put("category" + (j + 1), probabilities[i][j]);
            }
            database.insert(TABLE_NAME, null, values);
        }
    }

    private String getAttributeName(int attribute) {
        switch (attribute) {
            case 0:
                return "totalSleepTime";
            case 1:
                return "lightSleepTime";
            case 2:
                return "deepSleepTime";
            case 3:
                return "remSleepTime";
            case 4:
                return "efficiency";
            case 5:
                return "awakenings";
            case 6:
                return "suddenMovements";
            case 7:
                return "positionChanges";
            default:
                return "";
        }
    }
}
