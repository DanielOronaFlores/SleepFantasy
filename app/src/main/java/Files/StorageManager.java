package Files;

import android.os.Environment;
import android.os.StatFs;

public class StorageManager {
    public boolean hasSufficientStorage() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        double megabytesAvailable = bytesAvailable / (1024.0 * 1024.0);
        return megabytesAvailable > 1024.0;
    }
}
