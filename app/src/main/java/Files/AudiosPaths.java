package Files;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import java.io.File;

import AppContext.MyApplication;

@SuppressLint("SdCardPath")
public class AudiosPaths {
    public static String getRecordings3GPPath() {
        return MyApplication.getAppContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC) + "/recording.3gp";
    }

    public static String getRecordingsPCMPath() {
        return MyApplication.getAppContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC) + "/recording.pcm";
    }

    public static String getListSoundsPath() {
        return MyApplication.getAppContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/listSounds.xml";
    }

    public static String getMusicPath() {
        return "/sdcard/Music/";
    }
}
