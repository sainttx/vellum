/*
 * Copyright Evan Summers
 * 
 */
package vellum.datatype;

import java.util.HashMap;
import java.util.Map;
import vellum.type.ComparableTuple;

/**
 *
 * @author evan
 */
public class SimpleEntityCache implements EntityCache<Comparable> {
    Map<Comparable, Object> map = new HashMap();

    public static Comparable getComparable(Class type, Comparable id) {
        return new ComparableTuple(new Comparable[] {type.getName(), id});
    }
    
    public <V> void put(Comparable id, V value) {
        map.put(getComparable(value.getClass(), id), value);
    }
    
    public <V> V get(Class<V> type, Comparable id) {
        return (V) map.get(getComparable(type, id));
        
    }
}
