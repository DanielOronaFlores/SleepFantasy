package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseConnection extends SQLiteOpenHelper {
    private static DatabaseConnection instance;
    private static final String databaseName = "Sleep Fantasy";
    private static final int databaseVersion = 1;

    public static synchronized DatabaseConnection getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseConnection(context.getApplicationContext(), databaseName, null, databaseVersion);
        }
        return instance;
    }
    private DatabaseConnection(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        DatabaseTableCreator.createTables(db);
        DatabaseTableFiller.fillTables(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database schema upgrades here if needed.
    }

    public SQLiteDatabase getDatabase() {
        return this.getWritableDatabase();
    }

    public void openDatabase() {
        this.getWritableDatabase();
    }
    public void closeDatabase() {
        this.close();
    }
}
