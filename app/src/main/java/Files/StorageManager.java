package Files;

import android.os.Environment;
import android.os.StatFs;

public class StorageManager {
    public static boolean isInsufficientStorage() {
        float GIGABYTE = 1024.0f;

        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        double megabytesAvailable = bytesAvailable / (GIGABYTE * GIGABYTE);
        System.out.println("Available MB : " + megabytesAvailable);
        return megabytesAvailable < GIGABYTE;
    }
}
