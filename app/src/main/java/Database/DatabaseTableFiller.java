package Database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import Dates.DateManager;

public class DatabaseTableFiller {
    private static final DateManager dateManager = new DateManager();
    public static void fillTables(SQLiteDatabase db) {
        fillMissionTable(db);
        fillChallengeTable(db);
        fillRecordTable(db);
        fillMonsterTable(db);
        fillPlayListTable(db);
        fillSongsTable(db);
        fillPlaylistSongs(db);
        fillRewardsTable(db );
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

    private static void fillSongsTable(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        //Nature Songs
        values.put("name", "Lluvia");
        values.put("ibBySystem", true);
        db.insert("Songs", null, values);

        values.clear();
        values.put("name", "Rio");
        values.put("ibBySystem", true);
        db.insert("Songs", null, values);

        values.clear();
        values.put("name", "Oleaje");
        values.put("ibBySystem", true);
        db.insert("Songs", null, values);

        //Classical Songs
        values.clear();
        values.put("name", "Adagio");
        values.put("ibBySystem", true);
        db.insert("Songs", null, values);

        values.clear();
        values.put("name", "Clair De Lune");
        values.put("ibBySystem", true);
        db.insert("Songs", null, values);

        values.clear();
        values.put("name", "Pavane");
        values.put("ibBySystem", true);
        db.insert("Songs", null, values);

        //WhiteNoise Songs
        values.clear();
        values.put("name", "Marron");
        values.put("ibBySystem", true);
        db.insert("Songs", null, values);

        values.clear();
        values.put("name", "Rosa");
        values.put("ibBySystem", true);
        db.insert("Songs", null, values);

        values.clear();
        values.put("name", "Blanco");
        values.put("ibBySystem", true);
        db.insert("Songs", null, values);

        //Ambient Songs
        values.clear();
        values.put("name", "Cafe");
        values.put("ibBySystem", true);
        db.insert("Songs", null, values);

        values.clear();
        values.put("name", "Tren");
        values.put("ibBySystem", true);
        db.insert("Songs", null, values);

        values.clear();
        values.put("name", "Oficina");
        values.put("ibBySystem", true);
        db.insert("Songs", null, values);
    }

    private static void fillPlaylistSongs(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        // Nature Playlist
        values.put("playlistId", 1);
        values.put("songId", 1);
        db.insert("PlaylistSongs", null, values);

        values.clear();
        values.put("playlistId", 1);
        values.put("songId", 2);
        db.insert("PlaylistSongs", null, values);

        values.clear();
        values.put("playlistId", 1);
        values.put("songId", 3);
        db.insert("PlaylistSongs", null, values);

        // Classical Playlist
        values.clear();
        values.put("playlistId", 2);
        values.put("songId", 4);
        db.insert("PlaylistSongs", null, values);

        values.clear();
        values.put("playlistId", 2);
        values.put("songId", 5);
        db.insert("PlaylistSongs", null, values);

        values.clear();
        values.put("playlistId", 2);
        values.put("songId", 6);
        db.insert("PlaylistSongs", null, values);

        // WhiteNoise Playlist
        values.clear();
        values.put("playlistId", 3);
        values.put("songId", 7);
        db.insert("PlaylistSongs", null, values);

        values.clear();
        values.put("playlistId", 3);
        values.put("songId", 8);
        db.insert("PlaylistSongs", null, values);

        values.clear();
        values.put("playlistId", 3);
        values.put("songId", 9);
        db.insert("PlaylistSongs", null, values);

        // Ambient Playlist
        values.clear();
        values.put("playlistId", 4);
        values.put("songId", 10);
        db.insert("PlaylistSongs", null, values);

        values.clear();
        values.put("playlistId", 4);
        values.put("songId", 11);
        db.insert("PlaylistSongs", null, values);

        values.clear();
        values.put("playlistId", 4);
        values.put("songId", 12);
        db.insert("PlaylistSongs", null, values);
    }

    private static void fillRewardsTable(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        for (int i = 0; i < 20; i++) { //Type 1 = Audios
            values.put("type", 1);
            values.put("given", 0);
            db.insert("Rewards", null, values);
        }
    }
}
