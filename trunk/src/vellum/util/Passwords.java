/*
 * Copyright Evan Summers
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
public class Passwords {
    public static String ALGORITHM = "PBKDF2WithHmacSHA1";

    public static byte[] nextSalt() {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.setSeed(System.currentTimeMillis());
        random.nextBytes(salt);
        return salt;
    }
        
    public static String hashPassword(String password, byte[] salt) {
        salt = nextSalt();
        String saltHash = Base64.encode(salt);
        String hash = hashPasswordImpl(password, salt);
        return pack(password.length(), hash, saltHash);
    }
        
    public static boolean matches(String password, String passwordHash, String saltHash) {
        try {
            saltHash = unpackSaltHash(password.length(), passwordHash);
            String otherHash = unpackPasswordHash(password.length(), passwordHash);
            byte[] salt = decode(saltHash);
            String hash = hashPasswordImpl(password, salt);
            return hash.equals(otherHash);
        } catch (Exception e) {
            Exceptions.warn(e);
            return false;
        }
    }

    private static String pack(int length, String hash, String saltHash) {
        return hash.substring(0, length) + saltHash.substring(0, 22) + hash.substring(length);
    }

    private static String unpackPasswordHash(int length, String passwordHash) {
        return passwordHash.substring(0, length) + passwordHash.substring(length + 22);
    }

    private static String unpackSaltHash(int length, String passwordHash) {
        return passwordHash.substring(length, length + 22) + "==";
    }
        
    private static String hashPasswordImpl(String password, byte[] salt) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 2048, 160);
            SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = f.generateSecret(spec).getEncoded();
            return encode(hash);
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
    }
    
    public static String encode(byte[] bytes) {
        return new BASE64Encoder().encode(bytes);
    }

    public static byte[] decode(String string) throws IOException {
        return new BASE64Decoder().decodeBuffer(string);
    }    
}
