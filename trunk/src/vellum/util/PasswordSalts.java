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
public class PasswordSalts {
    public static final int SALT_LENGTH = 16;    
    public static final int ENCODED_SALT_LENGTH = 24;    
    
    public static byte[] nextSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }    
}
