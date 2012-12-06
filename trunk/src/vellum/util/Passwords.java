/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.util;

import java.security.SecureRandom;

/**
 *
 * @author evan
 */
public class Passwords {
    private static final PasswordSpec specs[] = {
        new PasswordSpec("PBKDF2WithHmacSHA1", 9999, 160)
    };
    public static final int LATEST_REVISION_INDEX = specs.length - 1;
    public static final int ENCODED_SALT_LENGTH = 24;    
    
    public static String hashPassword(char[] password, byte[] salt) {
        return hashPassword(password, salt, LATEST_REVISION_INDEX);
    }

    public static String hashPassword(char[] password, byte[] salt, int revisionIndex) {
        return hashPassword(password, salt, specs[revisionIndex]);
    }
    
    public static String hashPassword(char[] password, byte[] salt, PasswordSpec spec) {
        return spec.hashPassword(password, salt);
    }

    public static boolean matches(char[] password, String passwordHash, String salt) {
        return matches(password, passwordHash, salt, LATEST_REVISION_INDEX);
    }
    
    public static boolean matches(char[] password, String passwordHash, String salt, int revisionIndex) {
        byte[] saltBytes = Base64.decode(salt);
        String hash = hashPassword(password, saltBytes, revisionIndex);
        return hash.equals(passwordHash);
    }

    public static byte[] nextSalt() {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }
}
