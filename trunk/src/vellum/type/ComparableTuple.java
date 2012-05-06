/*
 * Copyright Evan Summers
 * 
 */
package vellum.type;

import vellum.util.Comparables;

/**
 *
 * @author evan
 */
public class ComparableTuple implements Comparable<ComparableTuple> {
    Comparable[] values;

    public ComparableTuple(Comparable[] values) {
        this.values = values;
    }
        
    @Override
    public int compareTo(ComparableTuple other) {
        return Comparables.compareTo(values, other.values);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ComparableTuple) {
            return compareTo((ComparableTuple) obj) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Comparables.hashCode(values);
    }
    
    public static ComparableTuple newInstance(Comparable ... values) {
        return new ComparableTuple(values);       
    }   
}
