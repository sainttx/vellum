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
import org.apache.catalina.util.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import vellum.exception.Exceptions;

/**
 *
 * @author evan
 */
public class SaltedPasswords {

    public static String hashPassword(String password) {
        byte[] saltBytes = Passwords.nextSalt();
        String salt = Base64.encode(saltBytes);
        String hash = Passwords.hashPassword(password, saltBytes);
        return pack(password.length(), hash, salt);
    }
        
    public static boolean matches(String password, String passwordHash, String saltHash) {
        try {
            saltHash = unpackSalt(password.length(), passwordHash);
            String otherHash = unpackPassword(password.length(), passwordHash);
            byte[] salt = Passwords.decode(saltHash);
            String hash = Passwords.hashPassword(password, salt);
            return hash.equals(otherHash);
        } catch (Exception e) {
            Exceptions.warn(e);
            return false;
        }
    }

    private static String pack(int length, String hash, String salt) {
        return hash.substring(0, length) + salt.substring(0, 22) + hash.substring(length);
    }

    private static String unpackPassword(int length, String passwordHash) {
        return passwordHash.substring(0, length) + passwordHash.substring(length + 22);
    }

    private static String unpackSalt(int length, String passwordHash) {
        return passwordHash.substring(length, length + 22) + "==";
    }        
}
