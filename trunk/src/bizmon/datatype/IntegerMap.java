/*
 * 
 * 
 */
package bizmon.datatype;

import java.util.TreeMap;

/**
 *
 * @author evanx
 */
public class IntegerMap<T> extends TreeMap<T, Integer> {
    
    public int getInt(T key) {
        Integer number = get(key);
        if (number == null) {
            number = new Integer(0);
        }
        return number;
    }

}
