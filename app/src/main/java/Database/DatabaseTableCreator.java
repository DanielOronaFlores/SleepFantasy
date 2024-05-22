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
        createPlaylistTable(db);
        createAudiosTable(db);
        createPlaylistAudiosTable(db);
        createProbabilitiesTable(db);
        createRewardsTable(db);
        createTipsTable(db);
    }

    private static void createRewardsTable(SQLiteDatabase db) {
        String query = "CREATE TABLE Rewards (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type INTEGER, " +
                "given BOOLEAN);";
        db.execSQL(query);
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
                "recordAudios BOOLEAN, " +
                "timerDuration INTEGER, " +
                "audioQuality BOOLEAN, " +
                "theme INTEGER, " +
                "skin INTEGER," +
                "notification INTEGER);";
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

    private static void createChallengeTable(SQLiteDatabase db) {
        String queryCreateChallengeTable = "CREATE TABLE Challenges (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Displayed BOOLEAN, " +
                "Completed BOOLEAN, " +
                "Counter INT, " +
                "Active BOOLEAN, " +
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
                "vigilTime INTEGER, " +
                "lightSleepTime INTEGER, " +
                "deepSleepTime INTEGER, " +
                "REMTime INTEGER, " +
                "totalSleepTime INTEGER, " +
                "efficiency INTEGER, " +
                "awakeningAmount INTEGER, " +
                "loudSoundsAmount INTEGER, " +
                "suddenMovementsAmount INTEGER, " +
                "positionChangesAmount INTEGER, " +
                "lightValue INTEGER, " +
                "date TEXT);";
        db.execSQL(queryCreateSleepDataTable);
    }

    private static void createMonsterTable(SQLiteDatabase db) {
        String queryCreateMonsterTable = "CREATE TABLE Monster (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "active BOOLEAN, " +
                "dateAppeared TEXT, " +
                "dateDisappeared TEXT);";
        db.execSQL(queryCreateMonsterTable);
    }

    private static void createPlaylistTable(SQLiteDatabase db) {
        String queryCreatePlaylistTable = "CREATE TABLE Playlist (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "createdBySystem BOOLEAN);";
        db.execSQL(queryCreatePlaylistTable);
    }

    private static void createAudiosTable(SQLiteDatabase db) {
        String queryCreateAudiosTable = "CREATE TABLE Audios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "createdBySystem BOOLEAN);";
        db.execSQL(queryCreateAudiosTable);
    }

    private static void createPlaylistAudiosTable(SQLiteDatabase db) {
        String queryCreatePlaylistAudiosTable = "CREATE TABLE PlaylistAudios (" +
                "playlistId INTEGER, " +
                "audioId INTEGER, " +
                "PRIMARY KEY (playlistId, audioId), " +
                "FOREIGN KEY (playlistId) REFERENCES Playlist(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (audioId) REFERENCES Audios(id) ON DELETE CASCADE);";
        db.execSQL(queryCreatePlaylistAudiosTable);
    }

    private static void createProbabilitiesTable(SQLiteDatabase db) {
        String queryCreateProbabilitiesTable = "CREATE TABLE Probabilities (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "attributeName TEXT, " +
                "range INTEGER, " +
                "category1 REAL, " +
                "category2 REAL, " +
                "category3 REAL, " +
                "category4 REAL, " +
                "category5 REAL, " +
                "category6 REAL, " +
                "category7 REAL); ";
        db.execSQL(queryCreateProbabilitiesTable);
    }

    private static void createTipsTable(SQLiteDatabase db) {
        String queryCreateTipsTable = "CREATE TABLE Tips (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "current BOOLEAN, " +
                "type INTEGER, " +
                "currentID INTEGER, " +
                "displayed BOOLEAN, " +
                "lastDateAppeared TEXT);";
        db.execSQL(queryCreateTipsTable);
    }
}