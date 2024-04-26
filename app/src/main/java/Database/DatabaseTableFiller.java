package Database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import Dates.DateManager;

public class DatabaseTableFiller {
    private static final DateManager dateManager = new DateManager();
    public static void fillTables(SQLiteDatabase db) {
        fillPreferencesTable(db);
        fillMissionTable(db);
        fillChallengeTable(db);
        fillRecordTable(db);
        fillMonsterTable(db);
        fillPlayListTable(db);
        fillAudiosTable(db);
        fillPlaylistAudios(db);
        fillRewardsTable(db);
        fillTipsTable(db);
    }
    private static void fillPreferencesTable(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("audioQuality", false);
        db.insert("Preferences", null, values);
    }
    private static void fillMissionTable(SQLiteDatabase db) {
        for (int i = 0; i < 20; i++) {
            ContentValues values = new ContentValues();
            values.put("currentDifficult", 1);
            values.put("currentCuantity", 0);

            if (i == 6 || i == 18 ) {
                values.put("requiredCuantity", 10);
            } else if (i == 12) {
                values.put("requiredCuantity", 1);
            } else {
                values.put("requiredCuantity", 3);
            }

            String currentDate = dateManager.getCurrentDate();
            values.put("date", currentDate);

            values.put("completed", false);
            db.insert("Missions", null, values);
        }
    }

    private static void fillChallengeTable(SQLiteDatabase db) {
        for (int i = 0; i < 15; i++) {
            ContentValues values = new ContentValues();
            values.put("Displayed", false);
            values.put("Completed", false);
            values.put("Counter", 0);
            values.put("Active", false);
            db.insert("Challenges", null, values);
        }
    }

    private static void fillRecordTable(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("isPlayingMusic", false);
        values.put("isTemporizerActive", false);
        values.put("hasMonsterAppeared", false);
        values.put("isCategoryValid", false);
        values.put("hasDeletedAudio", false);
        values.put("hasAvatarVisualChanged", false);
        values.put("isNewSoundSet", false);
        values.put("isNewInterface", false);
        values.put("isNewAudioUpoloaded", false);
        values.put("hasAudiosPlayed", false);
        values.put("isGraphDisplayed", false);
        values.put("hasObtainedExperience", false);
        db.insert("Records", null, values);
    }

    private static void fillMonsterTable(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        for (int i = 0; i < 5; i++) {
            values.put("id", i + 1);
            values.put("active", false);
            db.insert("Monster", null, values);
        }
    }

    private static void fillPlayListTable(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        // Playlist 1
        values.put("name", "Naturaleza");
        values.put("createdBySystem", true);
        db.insert("Playlist", null, values);

        // Playlist 2
        values.clear();
        values.put("name", "Clasica");
        values.put("createdBySystem", true);
        db.insert("Playlist", null, values);

        // Playlist 3
        values.clear();
        values.put("name", "Ruido Blanco");
        values.put("createdBySystem", true);
        db.insert("Playlist", null, values);

        // Playlist 4
        values.clear();
        values.put("name", "Ambiental");
        values.put("createdBySystem", true);
        db.insert("Playlist", null, values);
    }

    private static void fillAudiosTable(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        //Nature Audios
        values.put("name", "Lluvia");
        values.put("createdBySystem", true);
        db.insert("Audios", null, values);

        values.clear();
        values.put("name", "Rio");
        values.put("createdBySystem", true);
        db.insert("Audios", null, values);

        values.clear();
        values.put("name", "Oleaje");
        values.put("createdBySystem", true);
        db.insert("Audios", null, values);

        //Classical Audios
        values.clear();
        values.put("name", "Adagio");
        values.put("createdBySystem", true);
        db.insert("Audios", null, values);

        values.clear();
        values.put("name", "Clair De Lune");
        values.put("createdBySystem", true);
        db.insert("Audios", null, values);

        values.clear();
        values.put("name", "Pavane");
        values.put("createdBySystem", true);
        db.insert("Audios", null, values);

        //WhiteNoise Audios
        values.clear();
        values.put("name", "Marron");
        values.put("createdBySystem", true);
        db.insert("Audios", null, values);

        values.clear();
        values.put("name", "Rosa");
        values.put("createdBySystem", true);
        db.insert("Audios", null, values);

        values.clear();
        values.put("name", "Blanco");
        values.put("createdBySystem", true);
        db.insert("Audios", null, values);

        //Ambient Audios
        values.clear();
        values.put("name", "Cafe");
        values.put("createdBySystem", true);
        db.insert("Audios", null, values);

        values.clear();
        values.put("name", "Tren");
        values.put("createdBySystem", true);
        db.insert("Audios", null, values);

        values.clear();
        values.put("name", "Oficina");
        values.put("createdBySystem", true);
        db.insert("Audios", null, values);
    }

    private static void fillPlaylistAudios(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        // Nature Playlist
        values.put("playlistId", 1);
        values.put("audioId", 1);
        db.insert("PlaylistAudios", null, values);

        values.clear();
        values.put("playlistId", 1);
        values.put("audioId", 2);
        db.insert("PlaylistAudios", null, values);

        values.clear();
        values.put("playlistId", 1);
        values.put("audioId", 3);
        db.insert("PlaylistAudios", null, values);

        // Classical Playlist
        values.clear();
        values.put("playlistId", 2);
        values.put("audioId", 4);
        db.insert("PlaylistAudios", null, values);

        values.clear();
        values.put("playlistId", 2);
        values.put("audioId", 5);
        db.insert("PlaylistAudios", null, values);

        values.clear();
        values.put("playlistId", 2);
        values.put("audioId", 6);
        db.insert("PlaylistAudios", null, values);

        // WhiteNoise Playlist
        values.clear();
        values.put("playlistId", 3);
        values.put("audioId", 7);
        db.insert("PlaylistAudios", null, values);

        values.clear();
        values.put("playlistId", 3);
        values.put("audioId", 8);
        db.insert("PlaylistAudios", null, values);

        values.clear();
        values.put("playlistId", 3);
        values.put("audioId", 9);
        db.insert("PlaylistAudios", null, values);

        // Ambient Playlist
        values.clear();
        values.put("playlistId", 4);
        values.put("audioId", 10);
        db.insert("PlaylistAudios", null, values);

        values.clear();
        values.put("playlistId", 4);
        values.put("audioId", 11);
        db.insert("PlaylistAudios", null, values);

        values.clear();
        values.put("playlistId", 4);
        values.put("audioId", 12);
        db.insert("PlaylistAudios", null, values);
    }

    private static void fillRewardsTable(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        for (int i = 0; i < 20; i++) { //Type 1 = Audios
            values.put("type", 1);
            values.put("given", 0);
            db.insert("Rewards", null, values);
        }

        for (int i = 0; i < 5; i++) { //Type 2 = Interfaces
            values.put("type", 2);
            values.put("given", 0);
            db.insert("Rewards", null, values);
        }

        for (int i = 0; i < 15; i++) { //Type 3 = Notification Sounds
            values.put("type", 3);
            values.put("given", 0);
            db.insert("Rewards", null, values);
        }

        for (int i = 0; i < 10; i++) { //Type 4 = Avatars Skins
            values.put("type", 4);
            values.put("given", 0);
            db.insert("Rewards", null, values);
        }
    }

    private static void fillTipsTable(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        for (int i = 1; i <= 3; i++) {
            values.put("current", false);
            values.put("type", i);
            values.put("lastDateAppeared", dateManager.getCurrentDate());
            db.insert("Tips", null, values);
        }
    }
}
