package Audio;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

import AppContext.MyApplication;
import DataAccess.PreferencesDataAccess;
import Database.DatabaseConnection;
import Notifications.WarningsNotifications;

public class AudioManager {
    private PreferencesDataAccess preferencesDataAccess;
    private WarningsNotifications warningsNotifications = new WarningsNotifications();
    public AudioManager() {
        initializeDependencies();
    }

    private void initializeDependencies() {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance(MyApplication.getAppContext());
        databaseConnection.openDatabase();
        preferencesDataAccess = new PreferencesDataAccess(databaseConnection);
    }

    public boolean hasSufficientStorage() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        double megabytesAvailable = bytesAvailable / (1024 * 1024);
        return megabytesAvailable > 1024;
    }

    public boolean doesRecordingFileExist() {
        String fileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/record.3gp";
        File file = new File(fileName);
        return file.exists();
    }

    public void notifyLowStorage() {
        // TODO: Implementar método de notificación por bajo almacenamiento
        warningsNotifications.showLowStorageWarning();
    }

    public int getPreferredSamplingRate() {
        return preferencesDataAccess.getAudioQuality() ? 22000 : 48000;
    }

    public void transferRecordingToSmartphone() {
        // TODO: Implementar método de transferencia de grabación al smartphone
    }

    public boolean shouldSaveRecording() {
        return preferencesDataAccess.getSaveRecordings();
    }
}