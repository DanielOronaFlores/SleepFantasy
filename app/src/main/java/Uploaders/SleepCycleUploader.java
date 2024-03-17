package Uploaders;

import AppContext.MyApplication;
import Database.DataUpdates.SleepDataUpdate;
import Database.DatabaseConnection;

public class SleepCycleUploader {
    private final DatabaseConnection connection =DatabaseConnection.getInstance(MyApplication.getAppContext());
    private final SleepDataUpdate sleepDataUpdate = new SleepDataUpdate(connection);

    public void uploadLight(float lightValue) {
        connection.openDatabase();
        sleepDataUpdate.addLightValue(lightValue);
    }
}
