/*
 * Copyright Evan Summers
 * 
 */
package vellum.datatype;

/**
 *
 * @author evan
 */
public class TimeOfDay {
    int hour;
    int minute;
    int second;
    int millisecond;

    public TimeOfDay(int offsetHours, long millis) {
        millis += Millis.fromHours(offsetHours);
        millis %= Millis.fromDays(1);
        hour = (int) (millis/Millis.fromHours(1));
        minute = (int) ((millis % Millis.fromHours(1))/Millis.fromMinutes(1));
        second = (int) ((millis % Millis.fromMinutes(1))/Millis.fromSeconds(1));
        millisecond = (int) (millis % Millis.fromSeconds(1));
    }
    
    public TimeOfDay(int hour, int minute, int second, int millisecond) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.millisecond = millisecond;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getMilliseconds() {
        return millisecond;
    }

    public void setMilliseconds(int milliseconds) {
        this.millisecond = milliseconds;
    }

    public long toMillis() {
        long millis = Millis.fromHours(hour);
        millis += Millis.fromMinutes(minute);
        millis += Millis.fromMinutes(second);
        millis += millisecond;
        return millis;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d,%03d", hour, minute, second, millisecond);
    }
    
    
}
