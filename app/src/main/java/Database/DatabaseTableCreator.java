package Database;

import android.database.sqlite.SQLiteDatabase;

public class DatabaseTableCreator {
    public static void createTables(SQLiteDatabase db) {
        createAvatarTable(db);
        createPreferencesTable(db);
        createMissionTable(db);
        createChallengeTable(db);
        createRecordsTable(db);
        createSleepDataTable(db);
        createMonsterTable(db);
    }

    private static void createAvatarTable(SQLiteDatabase db) {
        String queryCreateAvatarTable = "CREATE TABLE Avatar (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "age INTEGER, " +
                "level INTEGER, " +
                "characterClass INTEGER, " +
                "characterPhase INTEGER, " +
                "currentExperience INTEGER, " +
                "requiredExperience INTEGER);";
        db.execSQL(queryCreateAvatarTable);
    }
    private static void createPreferencesTable(SQLiteDatabase db) {
        String queryCreatePreferencesTable = "CREATE TABLE Preferences ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "saveRecordings BOOLEAN, " +
                "recordSnorings BOOLEAN); ";
        db.execSQL(queryCreatePreferencesTable);
    }
    private static void createMissionTable(SQLiteDatabase db) {
        String queryCreateMissionTable = "CREATE TABLE Missions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "currentDifficult INTEGER, " +
                "currentCuantity INTEGER, " +
                "requiredCuantity INTEGER, " +
                "date TEXT, " +
                "completed BOOLEAN);";
        db.execSQL(queryCreateMissionTable);
    }

    //En proceso
    private static void createChallengeTable(SQLiteDatabase db) {
        String queryCreateChallengeTable = "CREATE TABLE Challenges (" +
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Displayed BOOLEAN, " +
                "Completed BOOLEAN, " +
                "Counter INT, " +
                "Active BOOLEAN, " +
        //AssignedDate (?)
                "OldDate TEXT " +
                ");";
        db.execSQL(queryCreateChallengeTable);
    }

    private static void createRecordsTable(SQLiteDatabase db) {
        String queryCreateRecordsTable = "CREATE TABLE Records (" +
                "isPlayingMusic BOOLEAN, " +
                "isTemporizerActive BOOLEAN, " +
                "hasMonsterAppeared BOOLEAN, " +
                "isCategoryValid BOOLEAN, " +
                "hasDeletedAudio BOOLEAN, " +
                "hasAvatarVisualChanged BOOLEAN, " +
                "isNewSoundSet BOOLEAN, " +
                "isNewInterface BOOLEAN, " +
                "isNewAudioUpoloaded BOOLEAN, " +
                "hasAudiosPlayed BOOLEAN, " +
                "isGraphDisplayed BOOLEAN, " +
                "hasObtainedExperience BOOLEAN " +
                "); ";
        db.execSQL(queryCreateRecordsTable);
    }

    private static void createSleepDataTable(SQLiteDatabase db) {
        String queryCreateSleepDataTable = "CREATE TABLE SleepData (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "efficiency INTEGER, " +
                "awakeningAmount INTEGER, " +
                "loudSoundsAmount INTEGER, " +
                "suddenMovementsAmount INTEGER, " +
                "snoringAmount INTEGER, " +
                "positionChangesAmount INTEGER, " +
                "sonoringTime INTEGER, " +
                "date TEXT);";
        db.execSQL(queryCreateSleepDataTable);
    }

    private static void createMonsterTable(SQLiteDatabase db) {
        String queryCreateMonsterTable = "CREATE TABLE Monster (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "active BOOLEAN, " +
                "dateAppeared TEXT);";
        db.execSQL(queryCreateMonsterTable);
    }
}