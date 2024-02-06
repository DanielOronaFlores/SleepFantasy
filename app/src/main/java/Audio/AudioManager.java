package Audio;

import AppContext.MyApplication;
import DataAccess.PreferencesDataAccess;
import Database.DatabaseConnection;
import Notifications.Notifications;

public class AudioManager {
    private PreferencesDataAccess preferencesDataAccess;
    private final Notifications warningsNotifications = new Notifications();

    public AudioManager() {
        initializeDependencies();
    }

    private void initializeDependencies() {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance(MyApplication.getAppContext());
        databaseConnection.openDatabase();
        preferencesDataAccess = new PreferencesDataAccess(databaseConnection);
    }

    //Metodos de la clase
    public void transferRecordingToSmartphone() {
        // TODO: Implementar método de transferencia de grabación al smartphone
    }

    public void notifyLowStorage() {
        warningsNotifications.showLowStorageNotification();
    }

    public int getPreferredSamplingRate() {
        return preferencesDataAccess.getAudioQuality() ? 22000 : 48000;
    }

    public boolean shouldSaveRecording() {
        return preferencesDataAccess.getSaveRecordings();
    }
}