package my.edu.utar.appointmentsystem.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeFormater {
    public static String formatDateTime(String dateTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = sdf.parse(dateTime);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, 8);
        Date newDate = c.getTime();
        sdf.applyPattern("EEEE (dd-MM-yyyy) h:mm a");
        String newDateString = sdf.format(newDate);
        return newDateString;
    }
}
