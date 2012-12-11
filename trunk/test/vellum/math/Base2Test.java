/*
 * Copyright Evan Summers
 * 
 */
package vellum.math;

import java.util.Arrays;
import org.junit.Test;
import vellum.crypto.Base2;
import static junit.framework.Assert.*;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class Base2Test {

    static Logr logger = LogrFactory.getLogger(Base2Test.class);

    @Test
    public void test() throws Exception {
        long[] pow2 = new long[63];
        for (int i = 0; i < pow2.length; i++) {
            pow2[i] = 1L<<i; 
            System.out.printf("%d, %d\n", i, pow2[i]);
        }
        assertEquals(4, Arrays.binarySearch(pow2, 16));
        assertEquals(1, Base2.pow(0));
        assertEquals(0, Base2.log(1));
        assertEquals(256, Base2.pow(8));
        assertEquals(8, Base2.log(256));
        assertEquals(256*256, Base2.pow(16));
        assertEquals(16, Base2.log(256*256));
        long value = 1;
        for (int i = 1; i < Base2.MAX_EXPONENT; i++) {
            value *= 2;
            assertEquals(value, Base2.pow(i));
            assertEquals(i, Base2.log(value));
        }
    }
}
