/*
 */
package vellum.datatype;

import java.util.ArrayList;

/**
 *
 * @author evans
 */
public class SampleList extends ArrayList<TimestampedSample> {
    
    public TimestampedSample get(int index) {
        return super.get(index);
        
    }
}
