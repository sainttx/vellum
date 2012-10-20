/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.datatype;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 *
 * @author evan
 */
public class TimestampedDequer<T extends Timestamped>  {
    long capacityMillis;
    long lastTimestamp;
    ArrayDeque<T> deque = new ArrayDeque();
    
    public TimestampedDequer(long capacityMillis) {
        this.capacityMillis = capacityMillis;
    }
    
    public synchronized void addLast(T element) {
        if (element.getTimestamp() == 0 || element.getTimestamp() < lastTimestamp) {
            deque.clear(); // throw our toys out the cot
        } else {
            lastTimestamp = element.getTimestamp();
            prune(lastTimestamp);
            deque.addLast(element);
        }
    }

    private void prune(long lastTimestamp) {
        while (deque.size() > 0 && 
                deque.getFirst().getTimestamp() <= lastTimestamp - capacityMillis) {
            deque.removeFirst();
        }
    }
    
    public synchronized Deque<T> snapshot(long lastTimestamp) {
        prune(lastTimestamp);
        return deque.clone();
    }
    
    public synchronized Deque<T> tail(int size) {
        Deque tail = new ArrayDeque();
        Iterator<T> it = deque.descendingIterator();
        for (int i = 0; i < size && it.hasNext(); i++) {
            tail.addFirst(it.next());
        }
        return tail;
    }    
    
    public synchronized Deque<T> tailDescending(int size) {
        Deque tail = new ArrayDeque();
        Iterator<T> it = deque.descendingIterator();
        for (int i = 0; i < size && it.hasNext(); i++) {
            tail.addLast(it.next());
        }
        return tail;
    }    
    
}
