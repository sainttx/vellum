/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 */
package vellum.util;

import vellum.exception.Exceptions;
import java.text.ParseException;
import static java.util.Calendar.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import vellum.datatype.SafeDateFormat;

/**
 *
 * @author evanx
 */
public class Calendars {

    public static final String millisTimestampPattern = "yyyy-MM-dd HH:mm:ss,SSS";
    public static final SafeDateFormat dateFormat = new SafeDateFormat("yyyy-MM-dd");
    public static final SafeDateFormat shortDateFormat = new SafeDateFormat("yyMMdd");
    public static final SafeDateFormat timestampFormat = new SafeDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
    public static final SafeDateFormat millisTimestampFormat = new SafeDateFormat(millisTimestampPattern);
    public static final SafeDateFormat timeFormat = new SafeDateFormat("HH:mm:ss");
    public static final SafeDateFormat shortTimeFormat = new SafeDateFormat("HH:mm");

    public static long getIntervalMillis(Date from, Date to) {
        return Math.abs(to.getTime() - from.getTime());
    }

    public static long toSeconds(long millis) {
        return millis / 1000;
    }

    public static long toMinutes(long millis) {
        return millis / 1000 / 60;
    }

    public static Date getYesterdayDate() {
        return getDate(-1);
    }

    public static Date getDate(int offset) {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, offset);
        return calendar.getTime();
    }

    public static boolean isToday(Date date) {
        Calendar calendar = newCalendar(date);
        Calendar today = new GregorianCalendar();
        if (calendar.get(Calendar.YEAR) != today.get(Calendar.YEAR)) {
            return false;
        }
        return (calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR));
    }

    public static Date nextDay(Date date) {
        Calendar calendar = newCalendar(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    public static Calendar newCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Calendar newCalendar() {
        Calendar calendar = Calendar.getInstance();
        return calendar;
    }

    public static Date newDate() {
        return new Date();
    }

    public static Calendar parseCalendar(SafeDateFormat dateFormat, String string) {
        return newCalendar(dateFormat.parse(string));
    }

    public static Date parse(SafeDateFormat dateFormat, String string) {
        return dateFormat.parse(string);
    }

    public static Date parseTimestampMillis(String string) {
        SafeDateFormat format = millisTimestampFormat;
        if (string.length() > format.getPattern().length()) {
            string = string.substring(0, format.getPattern().length());
        }
        return parse(format, string);
    }

    public static Date parseTimestamp(String string) {
        SafeDateFormat format = timestampFormat;
        if (string.length() > format.getPattern().length()) {
            string = string.substring(0, format.getPattern().length());
        }
        return parse(format, string);
    }

    public static Date parseDate(String string) {
        if (string.length() > timestampFormat.getPattern().length()) {
            string = string.substring(0, timestampFormat.getPattern().length());
        }
        if (string.length() == timestampFormat.getPattern().length()) {
            int index = string.indexOf(".");
            if (index == string.length() - 4) {
                string = string.substring(0, index) + "," + string.substring(index + 1);
            }
            return parse(timestampFormat, string);
        }
        return parse(dateFormat, string);
    }

    public static int getHourOfDay(Date date) {
        return newCalendar(date).get(HOUR_OF_DAY);
    }

    public static int getMinute(Date date) {
        return newCalendar(date).get(MINUTE);
    }

    public static String formatHH(Date date) {
        return String.format("%02d", newCalendar(date).get(HOUR_OF_DAY));
    }

    public static int getCurrentYear() {
        Calendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.YEAR);
    }

    public static void setTime(Calendar calendar, int hour, int minute, int second) {
        calendar.set(HOUR_OF_DAY, hour);
        calendar.set(MINUTE, minute);
        calendar.set(SECOND, second);
        calendar.set(MILLISECOND, 0);        
    }
    
    public static void setTime(Calendar calendar, Date time) {
        Calendar timeCal = newCalendar(time);
        setTime(calendar, timeCal.get(HOUR_OF_DAY), timeCal.get(MINUTE), timeCal.get(SECOND));
    }
}
