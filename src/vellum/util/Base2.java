/*
 * Copyright Evan Summers
 * 
 */
package vellum.util;

import java.util.Arrays;

/**
 *
 * @author evan
 */
public class Base2 {

    public static final int MAX_EXPONENT = 62;
    private static final long[] pow = new long[MAX_EXPONENT + 1];
    
    static {
        for (int i = 0; i < pow.length; i++) {
            pow[i] = pow(i); 
        }
    }

    public static long pow(int exponent) {
        return 1L<<exponent;
    }
    
    public static int log(long operand) {
        return Arrays.binarySearch(pow, operand);
    }
}
