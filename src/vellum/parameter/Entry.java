/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 */

package vellum.parameter;

import java.util.Map;
import vellum.util.Args;

/**
 *
 * @author evan.summers
 */
public class Entry<K, V> {
    K key;
    V value;

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Args.format(key, value);
    }
}
