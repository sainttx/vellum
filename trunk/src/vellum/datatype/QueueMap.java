/*
 * Apache Software License 2.0
 * Supported by iPay (Pty) Ltd, BizSwitch.net
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
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
