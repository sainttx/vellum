/*
 * 
 * 
 */
package bizmon.datatype;

import bizmon.util.Lists;
import java.util.TreeMap;

/**
 *
 * @author evanx
 */
public class IntegerCounterMap<T> extends IntegerMap {

    protected int totalCount = 0;

    public void increment(T key) {
        totalCount++;
        Integer number = getInt(key);
        put(key, new Integer(number.intValue() + 1));
    }

    public Iterable<T> keySetSorted() {
        return Lists.sortByValue(this).keySet();
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int calculateTotalCount() {
        int total = 0;
        for (Object key : keySet()) {
            total += getInt(key);
        }
        return total;
    }
}
