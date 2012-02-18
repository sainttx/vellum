/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 */
package vellum.util;

import java.util.HashMap;

/**
 *
 * @author evanx
 */
public class UniqueMap<K, V> extends HashMap<K, V> {

    public V put(K key, V value) {
        if (key == null) {
            throw new NullPointerException(Args.format(key, value));
        }
        V previous = super.put(key, value);
        if (previous != null) {
            throw new IllegalArgumentException(key.toString());
        }
        return previous;
    }
}
