/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.crypto;

import java.io.IOException;

/**
 *
 * @author evan
 */
public class PackedPasswords {
    
    public static byte[] hashPassword(char[] password) {
        return hashPassword(password, Passwords.ITERATION_COUNT_EXPONENT, Passwords.KEY_SIZE);
    }

    public static byte[] hashPassword(char[] password, int iterationCountExponent, int keySize) {
        return new PasswordHash(password, iterationCountExponent, keySize).pack();
    }

    public static boolean matches(char[] password, byte[] packedBytes) throws IOException {
        return new PasswordHash(packedBytes).matches(password);
    }
}
