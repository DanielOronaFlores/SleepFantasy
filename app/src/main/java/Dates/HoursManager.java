package Dates;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HoursManager {
    public String getCurrentHour() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }
}
