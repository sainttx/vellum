/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package common.entity;

/**
 *
 * @author evan
 */
public class Comparables {

    public static int compareTo(Comparable comparable, Comparable other) {
        if (comparable == other) return 0;
        if (other == null) return 1;
        if (comparable == null) return -1;
        return comparable.compareTo(other);
    }
    
    public static boolean equals(Comparable comparable, Comparable other) {
        return compareTo(comparable, other) == 0;
    }
}
