package Preferences;

import AppContext.MyApplication;
import Database.DataAccess.PreferencesDataAccess;
import Database.DatabaseConnection;

public class RecordingPreferences {
    private PreferencesDataAccess preferencesDataAccess;

    public RecordingPreferences() {
        initializeDependencies();
    }
    private void initializeDependencies() {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance(MyApplication.getAppContext());
        databaseConnection.openDatabase();
        preferencesDataAccess = new PreferencesDataAccess(databaseConnection);
    }

    public int getPreferredSamplingRate() {
        int LOW_SAMPLING_RATE = 22000;
        int HIGH_SAMPLING_RATE = 48000;
        return preferencesDataAccess.getAudioQuality() ? LOW_SAMPLING_RATE : HIGH_SAMPLING_RATE;
    }
    public boolean shouldSaveRecording() {
        return preferencesDataAccess.getSaveRecordings();
    }
}