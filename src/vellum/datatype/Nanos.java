/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.datatype;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author evan
 */
public class Nanos {

    public static long toMicos(long nanos) {
        return nanos/1000;
    }
    
    public static long toMillis(long nanos) {
        return nanos/1000/1000;
    }
    
    public static long toSeconds(long nanos) {
        return nanos/1000/1000/1000;
    }

    public static long toMinutes(long nanos) {
        return toSeconds(nanos)/60;
    }

    public static long toHours(long nanos) {
        return toSeconds(nanos)/60/60;
    }

    public static long toDays(long nanos) {
        return toHours(nanos)/24;
    }
    
    public static long fromSeconds(long seconds) {
        return seconds*1000*1000;
    }

    public static long fromMinutes(long minutes) {
        return fromSeconds(minutes*60);
    }

    public static long fromHours(long hours) {
        return fromMinutes(hours*60);
    }

    public static long fromDays(long days) {
        return TimeUnit.DAYS.toNanos(days);
    }
    
    public static boolean isElapsed(long nanos, long duration) {
        return elapsed(nanos) > duration;
    }

    public static long elapsed(long nanos) {
        return System.nanoTime() - nanos;
    }
    
    public static String formatMillis(long nanos) {
        return String.format("%.3fms", 1.*nanos/1000/1000);
    }
    
    public static String formatSeconds(long nanos) {
        return String.format("%.3fs", 1.*nanos/1000/1000/1000);
    }
}
