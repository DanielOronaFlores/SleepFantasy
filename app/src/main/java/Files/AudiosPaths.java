package Files;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import AppContext.MyApplication;

public class AudiosPaths {
    private final Context context = MyApplication.getAppContext();

    @SuppressLint("SdCardPath")
    public String getRecordings3GPPath() {
        return  "/sdcard/Music/";
    }

    public String getRecordingsPCMPath() {
        return context.getExternalFilesDir(Environment.DIRECTORY_MUSIC) + "/recording.pcm";
    }

    public String getListSoundsPath() {
        return context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/listSounds.xml";
    }
}
