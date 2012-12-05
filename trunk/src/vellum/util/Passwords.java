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
public class Passwords {
    public static String ALGORITHM = "PBKDF2WithHmacSHA1";

    public static byte[] nextSalt() {
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    public static String hashPassword(String password, byte[] salt) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 2048, 160);
            SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = f.generateSecret(spec).getEncoded();
            return encode(hash);
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
    }
        
    public static boolean matches(String password, String passwordHash, String salt) {
        try {
            byte[] saltBytes = decode(salt);
            String hash = hashPassword(password, saltBytes);
            return hash.equals(passwordHash);
        } catch (Exception e) {
            Exceptions.warn(e);
            return false;
        }
    }

    public static String encode(byte[] bytes) {
        return new BASE64Encoder().encode(bytes);
    }

    public static byte[] decode(String string) throws IOException {
        return new BASE64Decoder().decodeBuffer(string);
    }    
}
