/*
 */
package vellum.datatype;

import java.util.List;

/**
 *
 * @author evans
 */
public class TimestampedLists {

    public static long getFirstMillis(List<Timestamped> timestampedList) {
        long firstMillis = 0;
        for (Timestamped entry : timestampedList) {
            if (entry.getTimestamp() > 0 && (firstMillis == 0 || firstMillis > entry.getTimestamp())) {
                firstMillis = entry.getTimestamp();
            }
        }
        return firstMillis;
    }

    public static long getLastMillis(List<Timestamped> timestampedList) {
        long lastMillis = 0;
        for (Timestamped entry : timestampedList) {
            if (entry.getTimestamp() > 0 && (lastMillis == 0 || lastMillis < entry.getTimestamp())) {
                lastMillis = entry.getTimestamp();
            }
        }
        return lastMillis;
    }
}
