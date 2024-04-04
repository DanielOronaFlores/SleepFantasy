package Files;

import java.io.File;
import java.util.Date;

import Dates.DateManager;

public class FilesManager {
    private static final DateManager dateManager = new DateManager();
    public static String getLastDateModified(String path) {
        File file = new File(path);
        if (file.exists()) {
            long lastModified = file.lastModified();

            Date date = new Date(lastModified);
            String lastModifiedDate = dateManager.convertDate(String.valueOf(date));
            return dateManager.formatDate(String.valueOf(lastModifiedDate));
        }
        return null;
    }

    public static void deleteFiles(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }
}
