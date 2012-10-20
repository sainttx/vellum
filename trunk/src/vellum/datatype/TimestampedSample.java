/*
 */
package vellum.datatype;

/**
 *
 * @author evans
 */
public class TimestampedSample<K, T extends Timestamped> extends TimestampedDigester implements Timestamped {

    IntegerCounterMap<K> counterMap = new IntegerCounterMap();
            
    public TimestampedSample(long millis) {
        super(millis);
    }

    @Override
    public long getTimestamp() {
        return millis;
    }

    public IntegerCounterMap getCounterMap() {
        return counterMap;
    }    
}
