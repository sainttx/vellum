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
    public static final int HASH_LENGTH = 54;
    public static final String HASH_VERSION_PREFIX = Passwords.formatDefaultParam();
    private static final int PREFIX_LENGTH = 26;

    private static String pack(String hash, String salt) {
        return HASH_VERSION_PREFIX + salt + hash;
    }

    public static boolean isPacked(String passwordHash) {
        return passwordHash.length() == HASH_LENGTH && passwordHash.startsWith(HASH_VERSION_PREFIX);
    }

    public static String hashPassword(String password) {
        byte[] saltBytes = Passwords.nextSalt();
        String salt = Base64.encode(saltBytes);
        String hash = Passwords.hashPassword(password, saltBytes);
        return pack(hash, salt);
    }
        
    public static boolean matches(String password, String passwordHash) {
        String salt = unpackSalt(passwordHash);
        passwordHash = unpackPassword(passwordHash);
        String hash = Passwords.hashPassword(password, Base64.decode(salt));
        return hash.equals(passwordHash);
    }

    private static String unpackPassword(String passwordHash) {
        return passwordHash.substring(PREFIX_LENGTH);
    }

    private static String unpackSalt(String passwordHash) {
        return passwordHash.substring(HASH_VERSION_PREFIX.length(), PREFIX_LENGTH);
    }        
}
