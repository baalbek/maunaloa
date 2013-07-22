package maunaloa.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: rcs
 * Date: 23.11.12
 * Time: 09:45
 */
public class DateUtils {
    private static final DateFormat _formatter = new SimpleDateFormat("yyyy-MM-dd");
    private static final Date _toDay = new Date();
    private static final GregorianCalendar _cal0 = new GregorianCalendar();
    private static final GregorianCalendar _cal1 = new GregorianCalendar();
    private static int MILLIS_IN_DAY = 60 * 60 * 24 * 1000;

    static {
        _cal0.setTime(_toDay);
    }

    public static double diffDays(Date curDate) {
        _cal1.setTime(curDate);
        return ((_cal1.getTimeInMillis() - _cal0.getTimeInMillis()) / MILLIS_IN_DAY);
    }

    public static Date createDate(int year, int month, int day) {
        _cal1.set(Calendar.YEAR, year);
        _cal1.set(Calendar.MONTH, month-1);
        _cal1.set(Calendar.DATE, day);
        return _cal1.getTime();
    }

    public static Date parse(String dateStr) {
        try {
            return (Date)_formatter.parse(dateStr);
        } catch (ParseException ex) {
            Logger.getLogger(DateUtils.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
    public static String format(Date date) {
        return _formatter.format(date);
    }
}
