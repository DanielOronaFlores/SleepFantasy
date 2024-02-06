package Files;

import android.os.Environment;

import java.io.File;

public class AudiosFiles {
    public String getRecordingsPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/record.3gp";
    }

    public boolean doesRecordingFileExist() {
        String fileName = getRecordingsPath();
        File file = new File(fileName);
        return file.exists();
    }
}
