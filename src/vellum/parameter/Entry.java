/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 * Apache Software License 2.0
 * Supported by iPay (Pty) Ltd, BizSwitch.net
 */

package vellum.parameter;

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
