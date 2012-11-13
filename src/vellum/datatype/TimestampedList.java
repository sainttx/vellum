/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.datatype;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author evan
 */
public class TimestampedList<T extends Timestamped>  {
    long capacityMillis;
    LinkedList<T> linkedList = new LinkedList();
    
    public TimestampedList(long capacityMillis) {
        this.capacityMillis = capacityMillis;
    }
    
    public int size() {
        return linkedList.size();
    }

    public synchronized void add(T element) {
        prune(element.getTimestamp());
        linkedList.add(0, element);
    }

    private void prune(long latestTimestamp) {
        if (latestTimestamp == 0) latestTimestamp = System.currentTimeMillis();
        while (linkedList.size() > 0) {
            T last = linkedList.getLast();
            if (last.getTimestamp() >= latestTimestamp - capacityMillis) {
                return;
            }
            linkedList.remove(last);
        }
    }

    public List<T> snapshot() {
        return getList(System.currentTimeMillis());
    }
    
    public synchronized List<T> getList(long millis) {
        prune(millis);
        return new ArrayList(linkedList);
    }

    public synchronized List<T> last(int size) {
        if (linkedList.size() <= size) {
            return new ArrayList(linkedList);
        } else {
            return new ArrayList(linkedList.subList(0, size));
        }
    }    
}
