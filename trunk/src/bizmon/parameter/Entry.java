/*
 * (c) Copyright 2010, iPay (Pty) Ltd, Evan Summers
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 */

package bizmon.parameter;

import bizmon.util.Args;

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
        return Args.formatPrint(key, value);
    }

}
