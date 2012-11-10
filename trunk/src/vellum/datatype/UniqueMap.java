/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2011, iPay (Pty) Ltd, Evan Summers
 */
package vellum.datatype;

import vellum.format.ArgFormats;
import java.util.HashMap;

/**
 *
 * @author evanx
 */
public class UniqueMap<K, V> extends HashMap<K, V> {

    public V put(K key, V value) {
        if (key == null) {
            throw new NullPointerException(ArgFormats.formatter.formatArgs(key, value));
        }
        V previous = super.put(key, value);
        if (previous != null) {
            throw new IllegalArgumentException(key.toString());
        }
        return previous;
    }
}
