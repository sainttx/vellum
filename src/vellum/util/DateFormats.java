/*
 */
package vellum.util;

import vellum.datatype.SafeDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author evans
 */
public class DateFormats {

    public static final String millisTimestampPattern = "yyyy-MM-dd HH:mm:ss,SSS";
    public static final String millisTimePattern = "HH:mm:ss,SSS";
    public static final SafeDateFormat dateFormat = new SafeDateFormat("yyyy-MM-dd");
    public static final SafeDateFormat shortDateFormat = new SafeDateFormat("yyMMdd");
    public static final SafeDateFormat millisTimestampFormat = new SafeDateFormat(millisTimestampPattern);
    public static final SafeDateFormat millisTimeFormat = new SafeDateFormat(millisTimePattern);
    public static final SafeDateFormat timestampFormat = millisTimestampFormat;
    public static final SafeDateFormat timeFormat = new SafeDateFormat("HH:mm:ss");
    public static final SafeDateFormat shortTimeFormat = new SafeDateFormat("HH:mm");

    public static String formatTime(long millis) {
        if (millis == 0) return "";
        return millisTimeFormat.format(new Date(millis));
    }
    
    public static Date parse(SafeDateFormat dateFormat, String string) {
        return dateFormat.parse(string, null);
    }

    public static Date parseTimestampMillis(String string) {
        SafeDateFormat format = DateFormats.millisTimestampFormat;
        if (string.length() > format.getPattern().length()) {
            string = string.substring(0, format.getPattern().length());
        }
        return parse(format, string);
    }

    public static Date parseTimestamp(String string) {
        SafeDateFormat format = DateFormats.timestampFormat;
        if (string.length() > format.getPattern().length()) {
            string = string.substring(0, format.getPattern().length());
        }
        return parse(format, string);
    }

    public static Date parseDate(String string) {
        if (string.length() > DateFormats.timestampFormat.getPattern().length()) {
            string = string.substring(0, DateFormats.timestampFormat.getPattern().length());
        }
        if (string.length() == DateFormats.timestampFormat.getPattern().length()) {
            int index = string.indexOf(".");
            if (index == string.length() - 4) {
                string = string.substring(0, index) + "," + string.substring(index + 1);
            }
            return parse(DateFormats.timestampFormat, string);
        }
        return parse(DateFormats.dateFormat, string);
    }

    public static Calendar parseCalendar(SafeDateFormat dateFormat, String string) {
        return Calendars.newCalendar(dateFormat.parse(string, null));
    }        
}
