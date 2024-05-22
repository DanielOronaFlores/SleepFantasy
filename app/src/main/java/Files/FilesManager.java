package Files;

import java.io.File;
import java.util.Date;

import Dates.DateManager;

public class FilesManager {
    public static String getLastDateModified(String path) {
        File file = new File(path);
        if (file.exists()) {
            long lastModified = file.lastModified();

            Date date = new Date(lastModified);
            String lastModifiedDate = DateManager.formatDate(String.valueOf(date));
            return DateManager.formatDate(lastModifiedDate);
        }
        return null;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) file.delete();
    }
}
