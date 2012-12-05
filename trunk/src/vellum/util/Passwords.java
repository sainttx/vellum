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

    public static String hashPassword(String password, byte[] salt) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_SIZE);
            SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = f.generateSecret(spec).getEncoded();
            return encode(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static boolean matches(String password, String passwordHash, String salt) {
        byte[] saltBytes = decode(salt);
        String hash = hashPassword(password, saltBytes);
        return hash.equals(passwordHash);
    }

    public static byte[] nextSalt() {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    public static String encode(byte[] bytes) {
        return new BASE64Encoder().encode(bytes);
    }

    public static byte[] decode(String string) {
        try {
            return new BASE64Decoder().decodeBuffer(string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }    
}
