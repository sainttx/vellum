/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.util;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author evan
 */
public class Passwords {
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int ITERATION_COUNT = 9999;
    private static final int KEY_SIZE = 160;

    public static String formatDefaultParam() {
        return format(ALGORITHM, ITERATION_COUNT, KEY_SIZE);
    }
    
    public static String format(String algorithm, int iterationCount, int keySize) {
        return String.format("%s/%d/%d/");
    }
    
    public static String hashPassword(String password, byte[] salt) {
        return hashPassword(password.toCharArray(), salt, ITERATION_COUNT, KEY_SIZE);
    }
    
    public static String hashPassword(char[] password, byte[] salt, int iterationCount, int keySize) {
        KeySpec spec = new PBEKeySpec(password, salt, iterationCount, keySize);
        return hashPassword(spec);
    }
    
    public static String hashPassword(KeySpec spec) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.encode(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static boolean matches(String password, String passwordHash, String salt) {
        byte[] saltBytes = Base64.decode(salt);
        String hash = hashPassword(password, saltBytes);
        return hash.equals(passwordHash);
    }

    public static byte[] nextSalt() {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }
}
