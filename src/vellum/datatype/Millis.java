/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.datatype;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import vellum.exception.ParseRuntimeException;
import vellum.util.DateFormats;

/**
 *
 * @author evan
 */
public class Millis {
    
    public static long getIntervalMillis(Date from, Date to) {
        return Math.abs(to.getTime() - from.getTime());
    }
    
    public static long toSeconds(long millis) {
        return millis / 1000;
    }

    public static long toMinutes(long millis) {
        return millis / 1000 / 60;
    }

    public static long toHours(long millis) {
        return millis / 1000 / 60 / 60;
    }

    public static long toDays(long millis) {
        return millis / 1000 / 60 / 60 / 24;
    }
    
    public static long fromSeconds(long seconds) {
        return seconds*1000;
    }

    public static long fromMinutes(long minutes) {
        return minutes*60*1000;
    }

    public static long fromHours(long hours) {
        return hours*60*60*1000;
    }
    
    public static long fromDays(long days) {
        return days*24*60*60*1000;
    }
        
    public static long elapsedMillis(Date startTime) {
        long currentMillis = System.currentTimeMillis();
        if (startTime == null) return currentMillis;
        return currentMillis - startTime.getTime();
    }

    public static boolean isElapsed(Date startTime, long millis) {
        return elapsedMillis(startTime) > millis;
    }

    public static boolean isElapsed(long startMillis, long millis) {
        return (System.currentTimeMillis() - startMillis) > millis;
    }

    public static String formatTime(long millis) {
        if (millis == 0) return "00:00:00";
        return DateFormats.timeFormat.format(new Date(millis));
    }

    public static String formatTimestamp(long millis) {
        if (millis == 0) return "00:00:00,000";
        return DateFormats.timestampFormat.format(new Date(millis));
    }

    public static Long parse(String string) {
        int index = string.indexOf(" ");
        if (index > 0) {
            return TimeUnit.valueOf(string.substring(index + 1)).toMillis(Long.parseLong(string.substring(0, index)));
        } else if (string.length() >= 2 &&
                Character.isLowerCase(string.charAt(string.length() - 1)) && 
                Character.isDigit(string.charAt(string.length() - 2))) {            
            long value = Long.parseLong(string.substring(0, string.length() - 1));    
            if (string.endsWith("d")) {
                return TimeUnit.DAYS.toMillis(value);
            } else if (string.endsWith("h")) {
                return TimeUnit.HOURS.toMillis(value);
            } else if (string.endsWith("m")) {
                return TimeUnit.MINUTES.toMillis(value);
            } else if (string.endsWith("s")) {
                return TimeUnit.SECONDS.toMillis(value);
            }
        }        
        throw new ParseRuntimeException(string);
    }    
}
