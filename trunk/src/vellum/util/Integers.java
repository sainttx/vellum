/*
 * Apache Software License 2.0
 * Apache Software License 2.0, (c) Copyright 2013 Evan Summers
 */
package vellum.util;

/**
 *
 * @author evanx
 */
public class Integers {

    public static double log2(int n) {
        return Math.log(n) / Math.log(2);
    }
    
    public static long pow2(int n) {
        return 2 << n;
    }
    
}
