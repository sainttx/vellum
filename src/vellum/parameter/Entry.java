/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */

package vellum.parameter;

import java.util.Map;
import vellum.util.Args;

/**
 *
 * @author evanx
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
