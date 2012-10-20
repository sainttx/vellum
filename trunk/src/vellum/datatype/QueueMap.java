/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 */

package vellum.datatype;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author evanx
 */
public class QueueMap<K, V> extends HashMap<K, V> {
    int capacity;
    Queue<K> keyQueue = new LinkedList();

    public QueueMap(int capacity) {
        this.capacity = capacity;
    }

    public V put(K key, V value) {
        while (size() >= capacity) {
            super.remove(keyQueue.remove());
        }
        keyQueue.add(key);
        return super.put(key, value);
    }

}
