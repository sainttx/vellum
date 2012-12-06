/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.util;

/**
 *
 * @author evan
 */
public class PackedPasswords {
    public static final int REVISION_PREFIX_LENGTH = 2;

    private static String pack(String hash, String salt, int revisionIndex) {
        assert revisionIndex >= 0 && revisionIndex <= 9;
        return "^" + revisionIndex + salt + hash;
    }

    public static boolean isPacked(String passwordHash) {
        return passwordHash.charAt(0) == '^';
    }

    public static boolean isPackedLatest(String passwordHash) {
        return passwordHash.charAt(0) == '^' && passwordHash.charAt(1) == '0' + Passwords.LATEST_REVISION_INDEX;
    }
    
    public static int unpackRevisionIndex(String passwordHash) {
        return passwordHash.charAt(1) - '0';
    }

    private static String unpackSalt(String passwordHash) {
        return passwordHash.substring(REVISION_PREFIX_LENGTH, REVISION_PREFIX_LENGTH + Passwords.ENCODED_SALT_LENGTH);
    }        
        
    private static String unpackPassword(String passwordHash) {
        return passwordHash.substring(REVISION_PREFIX_LENGTH + Passwords.ENCODED_SALT_LENGTH);
    }

    public static String hashPassword(char[] password) {
        return hashPassword(password, Passwords.LATEST_REVISION_INDEX);
    }
    
    public static String hashPassword(char[] password, int revisionIndex) {
        byte[] saltBytes = Passwords.nextSalt();
        String salt = Base64.encode(saltBytes);
        String hash = Passwords.hashPassword(password, saltBytes);
        return pack(hash, salt, revisionIndex);
    }
        
    public static boolean matches(char[] password, String passwordHash) {
        int revisionIndex = unpackRevisionIndex(passwordHash);
        String salt = unpackSalt(passwordHash);
        passwordHash = unpackPassword(passwordHash);
        String hash = Passwords.hashPassword(password, Base64.decode(salt), revisionIndex);
        return hash.equals(passwordHash);
    }
    
}
