/*
 * Copyright Evan Summers
 * 
 */
package vellum.datatype;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author evan
 */
public class MapEntry<K, V> implements Map.Entry<K, V> {
    K key;
    V value;

    public MapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V oldValue = value;
        this.value = value;
        return oldValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MapEntry) {
            MapEntry other = (MapEntry) o;
            return key.equals(other.key) && value.equals(other.value);
        }
        return false;
                

    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
