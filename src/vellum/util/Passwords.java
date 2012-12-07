/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.util;

import java.util.Arrays;

/**
 *
 * @author evan
 */
public class Passwords {
    public static final PasswordSpec SPECS[] = {
        new PasswordSpec("PBKDF2WithHmacSHA1", 1000, 128),
        new PasswordSpec("PBKDF2WithHmacSHA1", 9999, 160)
    };
    public static final int LATEST_REVISION_INDEX = SPECS.length - 1;

    public static PasswordSpec getSpec() {
        return SPECS[LATEST_REVISION_INDEX];
    }
    
    public static PasswordSpec getSpec(int revisionIndex) {
        return SPECS[revisionIndex];
    }
    
    public static byte[] hashPassword(char[] password, byte[] salt) {
        return hashPassword(password, salt, LATEST_REVISION_INDEX);
    }
    
    public static byte[] hashPassword(char[] password, byte[] salt, int revisionIndex) {
        return SPECS[revisionIndex].hashPassword(password, salt);
    }

    public static boolean matches(char[] password, byte[] passwordHash, byte[] salt) {
        return matches(password, passwordHash, salt, LATEST_REVISION_INDEX);
    }
    
    public static boolean matches(char[] password, byte[] passwordHash, byte[] salt, int revisionIndex) {
        return Arrays.equals(passwordHash, hashPassword(password, salt, revisionIndex));
    }
}
