/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.util;

import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author evan
 */
public class Passwords {

    public static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    public static final int ITERATION_COUNT_EXPONENT = 12;
    public static final int KEY_SIZE = 160;

    public static byte[] hashPassword(char[] password, byte[] salt) {
        return hashPassword(password, salt, ITERATION_COUNT_EXPONENT, KEY_SIZE);
    }
    
    public static byte[] hashPassword(char[] password, byte[] salt, int iterationCountExponent, int keySize) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, 2 << iterationCountExponent, keySize);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            return factory.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean matches(char[] password, byte[] passwordHash, byte[] salt) {
        return matches(password, passwordHash, salt, ITERATION_COUNT_EXPONENT, KEY_SIZE);
    }
    
    public static boolean matches(char[] password, byte[] passwordHash, byte[] salt, int iterationCountExponent, int keySize) {
        return Arrays.equals(passwordHash, hashPassword(password, salt, iterationCountExponent, keySize));
    }
}
