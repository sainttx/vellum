/*
 */
package vellum.datatype;

/**
 *
 * @author evans
 */
public class TimestampedDigester<T extends Timestamped> {

    long millis = System.currentTimeMillis();
    long firstMillis;
    long lastMillis;
    long totalMillis;
    int sampleSize;

    public TimestampedDigester(long millis) {
        this.millis = millis;
    }

    public void digest(T timestamped) {
        if (firstMillis == 0 || firstMillis > timestamped.getTimestamp()) {
            firstMillis = timestamped.getTimestamp();
        }
        if (lastMillis == 0 || lastMillis < timestamped.getTimestamp()) {
            lastMillis = timestamped.getTimestamp();
        }
        totalMillis += millis - timestamped.getTimestamp();
        sampleSize++;
    }

    public long getFirstMillis() {
        return firstMillis;
    }

    public void setFirstMillis(long firstMillis) {
        this.firstMillis = firstMillis;
    }

    public long getLastMillis() {
        return lastMillis;
    }

    public void setLastMillis(long lastMillis) {
        this.lastMillis = lastMillis;
    }

    public int getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
    }
        
    public long getAverageMillis() {
        if (sampleSize == 0) {
            return 0;
        }
        return millis - totalMillis/sampleSize;
    }
}
