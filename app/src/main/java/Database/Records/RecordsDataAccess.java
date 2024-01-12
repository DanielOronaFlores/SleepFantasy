package Database.Records;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import Database.DatabaseConnection;

public class RecordsDataAccess {
    private static SQLiteDatabase database;

    public RecordsDataAccess(DatabaseConnection connection) {
        this.database = connection.getDatabase();;
    }

    public boolean isPlayingMusic() {
        String columnName = "isPlayingMusic";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean playingMusic = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            playingMusic = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return playingMusic;
    }
    public boolean isTemporizerActive() {
        String columnName = "isTemporizerActive";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean temporizerActive = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            temporizerActive = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return temporizerActive;
    }
    public boolean hasMonsterAppeared() { //TODO: Si llega a aparecer un monstruo se debe poner true
        String columnName = "hasMonsterAppeared";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean monsterAppeared = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            monsterAppeared = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return monsterAppeared;
    }
    public boolean isCategoryValid() { //TODO: Al acabar el ciclo de sueno se debe poner true si es categoria 3
        String columnName = "isCategoryValid";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean categoryValid = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            categoryValid = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return categoryValid;
    }
    public boolean isDeletedAudio() { //TODO: Al eliminar un audio se debe poner true
        String columnName = "isDeletedAudio";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean deletedAudio = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            deletedAudio = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return deletedAudio;
    }
    public boolean hasAvatarVisualChanged() { //TODO: Al cambiar la skin del avatar se debe poner true
        String columnName = "hasAvatarVisualChanged";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean avatarVisualChanged = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            avatarVisualChanged = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return avatarVisualChanged;
    }
    public boolean isNewSoundSet() { //TODO: Al cambiar el sonido de notificacion se debe poner true
        String columnName = "isNewSoundSet";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean newSoundSet = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            newSoundSet = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return newSoundSet;
    }
    public boolean isNewInterface() { //TODO: Al cambiar la interfaz se debe poner true
        String columnName = "isNewInterface";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean newInterface = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            newInterface = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return newInterface;
    }
    public boolean isNewAudioUploaded() { //TODO: Al subir un audio se debe poner true
        String columnName = "isNewAudioUploaded";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean newAudioUploaded = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            newAudioUploaded = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return newAudioUploaded;
    }
    public boolean hasAudiosPlayed() { //TODO: Al reproducir tres audios se debe poner true
        String columnName = "hasAudiosPlayed";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean audiosPlayed = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            audiosPlayed = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return audiosPlayed;
    }
    public boolean isGraphDisplayed() { //TODO: Al mostrar el grafico diaro se debe poner true
        String columnName = "isGraphDisplayed";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean graphDisplayed = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            graphDisplayed = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return graphDisplayed;
    }
    public boolean hasObtainedExperience() { //TODO: Al obtener 500 experiencia se debe poner true
        String columnName = "hasObtainedExperience";
        Cursor cursor = database.query("Records", new String[]{columnName}, null, null, null, null, null);
        boolean obtainedExperience = false;

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(columnName);
            obtainedExperience = cursor.getInt(columnIndex) == 1;
        }

        cursor.close();
        return obtainedExperience;
    }
}
