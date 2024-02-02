package Audio;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class RecordingConditions {
    private int setSamplingRate(boolean isHighQuality) {
        if (isHighQuality) return 48000;
        else return 32000;
    }

    private boolean itHasAvailableStorage() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        double megabytesAvailable = bytesAvailable / (1024 * 1024);
        return megabytesAvailable > 1024;
    }

    public boolean checkIfFileExists() {
        String fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileName += "/recording.3gp";

        File file = new File(fileName);

        return file.exists();
    }
}
