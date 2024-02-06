package Files;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import AppContext.MyApplication;

public class AudiosFiles {
    private final Context context = MyApplication.getAppContext();

    public String getRecordingsPath() {
        return context.getExternalFilesDir(Environment.DIRECTORY_MUSIC) + "/recording.3gp";
    }

    public boolean doesRecordingFileExist() {
        String fileName = getRecordingsPath();
        File file = new File(fileName);
        return file.exists();
    }
}
