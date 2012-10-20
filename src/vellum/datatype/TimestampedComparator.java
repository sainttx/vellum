/*
 * Copyright Evan Summers
 * 
 */
package vellum.datatype;

import java.util.Comparator;

/**
 *
 * @author evan
 */
public class TimestampedComparator implements Comparator<Timestamped> {

    @Override
    public int compare(Timestamped o1, Timestamped o2) {
        if (o1.getTimestamp() < o2.getTimestamp()) return -1;
        if (o1.getTimestamp() > o2.getTimestamp()) return 1;
        else return 0;
    }    
}
