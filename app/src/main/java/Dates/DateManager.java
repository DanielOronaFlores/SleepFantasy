package Dates;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateManager {
    public static String convertDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        SimpleDateFormat newDateFormat;
        String formattedDate = null;
        try {
            if (date != null) {
                Date convertedDate = dateFormat.parse(date);
                newDateFormat = getDateFormat();
                formattedDate = newDateFormat.format(convertedDate);
                System.out.println("Fecha formateada: " + formattedDate);
            }
        } catch (ParseException e) {
            System.out.println("Sin fecha");
        }
        return formattedDate;
    }

    private static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    public static String getCurrentDate() {
        Date currentDate = new Date(System.currentTimeMillis());
        return getDateFormat().format(currentDate);
    }

    public static String getDateNextDays(String date, int days) {
        SimpleDateFormat sdf = getDateFormat();
        String endDate;
        try {
            Date inputDate = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            assert inputDate != null;
            calendar.setTime(inputDate);
            calendar.add(Calendar.DAY_OF_YEAR, days);
            endDate = sdf.format(calendar.getTime());
        } catch (ParseException e) {
            return null;
        }
        return endDate;
    }

    public static boolean hasPassedHoursSince(String oldDateStr, int hours) {
        boolean havePassed24Hours;
        long differenceHours = 0;

        try {
            Date currentDateFormatted = new Date(System.currentTimeMillis());
            Date oldDateFormatted = getDateFormat().parse(oldDateStr);

            if (oldDateFormatted == null) {
                throw new ParseException("Error parsing date", 0);
            }

            long differenceMillis = currentDateFormatted.getTime() - oldDateFormatted.getTime();
            differenceHours = differenceMillis / (60 * 60 * 1000);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            havePassed24Hours = differenceHours >= hours;
        }
        return havePassed24Hours;
    }

    public static long getDaysDifference(String startDate, String endDate) {
        Date startDateFormatted;
        Date endDateFormatted;

        try {
            startDateFormatted = getDateFormat().parse(startDate);
            endDateFormatted = getDateFormat().parse(endDate);
        } catch (ParseException e) {
            return 0;
        }

        if (startDateFormatted == null || endDateFormatted == null) {
            throw new RuntimeException("Error parsing dates");
        }

        long differenceMillis = endDateFormatted.getTime() - startDateFormatted.getTime();
        System.out.println("Dias de diferencia: " + differenceMillis / (24 * 60 * 60 * 1000));
        return Math.abs(differenceMillis / (24 * 60 * 60 * 1000));
    }

    public static String getDateSinceDate(int days) {
        SimpleDateFormat sdf = getDateFormat();
        String startDate;
        try {
            Date currentDate = new Date(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_YEAR, -days);
            startDate = sdf.format(calendar.getTime());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return startDate;
    }

    public static String getPastDaySinceCurrentDate(int day) {
        SimpleDateFormat sdf = getDateFormat();
        String startDate;
        try {
            Date currentDate = new Date(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_YEAR, -day);
            startDate = sdf.format(calendar.getTime());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return startDate;
    }

    public static boolean isConsecutive(String currentDate, String oldDate) {
        Date currentDateFormatted;
        Date oldDateFormatted;
        long differenceDays;

        try {
            currentDateFormatted = getDateFormat().parse(currentDate);
            oldDateFormatted = getDateFormat().parse(oldDate);

            if (currentDateFormatted == null || oldDateFormatted == null) {
                throw new ParseException("Error parsing dates", 0);
            }

            long differenceMillis = currentDateFormatted.getTime() - oldDateFormatted.getTime();
            differenceDays = differenceMillis / (24 * 60 * 60 * 1000); // 24 hours in a day, 60 minutes in an hour, 60 seconds in a minute, 1000 milliseconds in a second
        } catch (ParseException e) {
            return false;
        }
        return Math.abs(differenceDays) == 1;
    }

    public static String convertToFormat(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        Date dateFormatted;

        try {
            dateFormatted = getDateFormat().parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        assert dateFormatted != null;
        return sdf.format(dateFormatted);
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

    public static String formatDate(String date) {
        Date dateFormatted;

        try {
            dateFormatted = getDateFormat().parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return getDateFormat().format(dateFormatted);
    }
}
