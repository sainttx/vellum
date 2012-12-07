/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.util;

import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author evan
 */
public class PackedPasswords {
    
    public static byte[] hashPassword(char[] password) {
        return hashPassword(password, Passwords.LATEST_REVISION_INDEX);
    }
    
    public static byte[] hashPassword(char[] password, int revisionIndex) {
        byte[] salt = Passwords.getSpec().nextSalt();
        byte[] hash = Passwords.hashPassword(password, salt, revisionIndex);
        return new PasswordHash(hash, salt, revisionIndex).pack();
    }
        
    public static boolean matches(char[] password, byte[] packedBytes) throws IOException {
        PasswordHash passwordHash = new PasswordHash(packedBytes);
        byte[] hash = Passwords.hashPassword(password, passwordHash.getSalt(), passwordHash.getRevisionIndex());
        return Arrays.equals(hash, passwordHash.getHash());
    }

}
