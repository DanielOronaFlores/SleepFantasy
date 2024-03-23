package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseConnection extends SQLiteOpenHelper {
    private static volatile DatabaseConnection instance;
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

    public static class AvatarCreator {
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
}
