/*
 */
package vellum.util;

import vellum.datatype.SafeDateFormat;
import java.util.Date;

/**
 *
 * @author evans
 */
public class DefaultDateFormats {

    public static final SafeDateFormat dateFormat = new SafeDateFormat("yyyy-MM-dd");
    public static final SafeDateFormat timeSecondsFormat = new SafeDateFormat("HH:mm:ss");
    public static final SafeDateFormat timeMillisFormat = new SafeDateFormat("HH:mm:ss,SSS");
    public static final SafeDateFormat dateTimeSecondsFormat = new SafeDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SafeDateFormat dateTimeMillisFormat = new SafeDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

    public static String formatDateTimeSeconds(long millis) {
        if (millis == 0) return "";
        return dateTimeSecondsFormat.format(new Date(millis));
    }
    
    public static Date parseTimestampMillis(String string) {
        SafeDateFormat format = DefaultDateFormats.timeMillisFormat;
        if (string.length() > format.getPattern().length()) {
            string = string.substring(0, format.getPattern().length());
        }
        return format.parse(string);
    }

    public static Date parseTimestamp(String string) {
        SafeDateFormat format = DefaultDateFormats.timeMillisFormat;
        if (string.length() > format.getPattern().length()) {
            string = string.substring(0, format.getPattern().length());
        }
        return format.parse(string);
    }

    public static Date parseDate(String string) {
        if (string.length() > DefaultDateFormats.timeMillisFormat.getPattern().length()) {
            string = string.substring(0, DefaultDateFormats.timeMillisFormat.getPattern().length());
        }
        if (string.length() == DefaultDateFormats.timeMillisFormat.getPattern().length()) {
            return timeMillisFormat.parse(string);
        }
        return dateFormat.parse(string);
    }
}
