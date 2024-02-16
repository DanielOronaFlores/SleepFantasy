package Dates;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateManager {
    private SimpleDateFormat getDateFormat() {
        return  new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    private boolean isConsecutive(Date currentDate, Date oldDate) {
        long differenceMillis = currentDate.getTime() - oldDate.getTime();
        long differenceDays = differenceMillis / (24 * 60 * 60 * 1000); // 24 hours in a day, 60 minutes in an hour, 60 seconds in a minute, 1000 milliseconds in a second
        return Math.abs(differenceDays) == 1;
    }

    public String getCurrentDate() {
        Date currentDate = new Date(System.currentTimeMillis());
        return getDateFormat().format(currentDate);
    }

    public boolean compareDates(String oldDateStr) throws ParseException {
        String currentDateStr = getCurrentDate();
        Date currentDateFormatted = getDateFormat().parse(currentDateStr);
        Date oldDateFormatted = getDateFormat().parse(oldDateStr);

        if (currentDateFormatted == null || oldDateFormatted == null) {
            throw new ParseException("Error parsing dates", 0);
        }

        return isConsecutive(currentDateFormatted, oldDateFormatted);
    }

    public boolean isSunday() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        return dayOfWeek == Calendar.SUNDAY;
    }



    public boolean haveThreeDaysPassed(String endDateStr) throws ParseException {
        Date startDateFormatted = getDateFormat().parse(getCurrentDate());
        Date endDateFormatted = getDateFormat().parse(endDateStr);

        if (startDateFormatted == null || endDateFormatted == null) {
            throw new ParseException("Error parsing dates", 0);
        }

        long differenceMillis = endDateFormatted.getTime() - startDateFormatted.getTime();
        long differenceDays = differenceMillis / (24 * 60 * 60 * 1000);

        return differenceDays > 3;
    }

    public long getDaysDifference(String startDateStr, String endDateStr) throws ParseException {
        Date startDateFormatted = getDateFormat().parse(startDateStr);
        Date endDateFormatted = getDateFormat().parse(endDateStr);

        if (startDateFormatted == null || endDateFormatted == null) {
            throw new ParseException("Error parsing dates", 0);
        }

        long differenceMillis = endDateFormatted.getTime() - startDateFormatted.getTime();
        return differenceMillis / (24 * 60 * 60 * 1000);
    }

    public boolean havePassed24Hours(String oldDateStr) throws ParseException {
        Date currentDateFormatted = new Date(System.currentTimeMillis());
        Date oldDateFormatted = getDateFormat().parse(oldDateStr);

        if (oldDateFormatted == null) {
            throw new ParseException("Error parsing date", 0);
        }

        long differenceMillis = currentDateFormatted.getTime() - oldDateFormatted.getTime();
        long differenceHours = differenceMillis / (60 * 60 * 1000);

        return differenceHours >= 24;
    }

    public String getPastWeek(String date) {
        SimpleDateFormat sdf = getDateFormat();
        String startDate;
        try {
            Date inputDate = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(inputDate);
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            startDate = sdf.format(calendar.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return startDate;
    }
    public String getPastMonth(String date) {
        SimpleDateFormat sdf = getDateFormat();
        String startDate;
        try {
            Date inputDate = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(inputDate);
            calendar.add(Calendar.MONTH, -1);
            startDate = sdf.format(calendar.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return startDate;
    }

    public String formatDate(String date) {
        Date dateFormatted;

        try {
            dateFormatted = getDateFormat().parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return getDateFormat().format(dateFormatted);
    }

    public String monthDayOnly(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd", Locale.getDefault());
        Date dateFormatted;

        try {
            dateFormatted = getDateFormat().parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return sdf.format(dateFormatted);
    }
}
