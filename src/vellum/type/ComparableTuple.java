/*
 * Copyright Evan Summers
 * 
 */
package vellum.type;

import vellum.util.Args;
import vellum.util.Comparables;
import vellum.util.formatter.ArgFormatter;

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

    @Override
    public String toString() {
        return ArgFormatter.formatter.format(values);
    }
    
    public static ComparableTuple newInstance(Comparable ... values) {
        return new ComparableTuple(values);       
    }   
}
