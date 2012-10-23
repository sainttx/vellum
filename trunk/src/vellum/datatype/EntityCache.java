/*
 * Copyright Evan Summers
 * 
 */
package vellum.datatype;

/**
 *
 * @author evan
 */
public interface EntityCache<K extends Comparable> {
    public <V> V get(Class<V> type, K id);    
}
