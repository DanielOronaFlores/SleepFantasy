package Dates;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateManager {
    private SimpleDateFormat getDateFormat() {
        return  new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    public String getCurrentDate() {
        Date currentDate = new Date(System.currentTimeMillis());
        String currentDateStr = getDateFormat().format(currentDate);

        return currentDateStr;
    }

    public boolean compareDates(String oldDateStr) throws ParseException {
        String currentDateStr = getCurrentDate();
        Date currentDateFormatted;
        Date oldDateFormatted;

        currentDateFormatted = getDateFormat().parse(currentDateStr);
        oldDateFormatted = getDateFormat().parse(oldDateStr);

        Log.d("Dates", "Current date: " + currentDateFormatted);
        Log.d("Dates", "Old date: " + oldDateFormatted);

        return isConsecutive(currentDateFormatted, oldDateFormatted);
    }

    public boolean isSunday() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        return dayOfWeek == Calendar.SUNDAY;
    }

    private boolean isConsecutive(Date currentDate, Date oldDate) {
        long differenceMillis = currentDate.getTime() - oldDate.getTime();
        long differenceDays = differenceMillis / (24 * 60 * 60 * 1000);
        return Math.abs(differenceDays) == 1;
    }


    public boolean haveThreeDaysPassed(String endDateStr) throws ParseException {
        Date startDateFormatted = getDateFormat().parse(getCurrentDate());
        Date endDateFormatted = getDateFormat().parse(endDateStr);

        long differenceMillis = endDateFormatted.getTime() - startDateFormatted.getTime();
        long differenceDays = differenceMillis / (24 * 60 * 60 * 1000);

        return differenceDays > 3;
    }

    public long getDaysDifference(String startDateStr, String endDateStr) throws ParseException {
        Date startDateFormatted = getDateFormat().parse(startDateStr);
        Date endDateFormatted = getDateFormat().parse(endDateStr);

        long differenceMillis = endDateFormatted.getTime() - startDateFormatted.getTime();
        return differenceMillis / (24 * 60 * 60 * 1000);
    }

    public String calculateThreeDaysAhead(String startDateStr) throws ParseException {
        Date startDateFormatted = getDateFormat().parse(startDateStr);

        // Crear un objeto Calendar y establecer la fecha de inicio
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDateFormatted);

        // Sumar tres días a la fecha de inicio
        calendar.add(Calendar.DAY_OF_MONTH, 3);

        // Obtener la nueva fecha después de sumar tres días
        Date threeDaysAhead = calendar.getTime();

        // Formatear la nueva fecha como una cadena
        return getDateFormat().format(threeDaysAhead);
    }


}
