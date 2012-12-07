/*
 * Copyright Evan Summers
 * 
 */
package vellum.util;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author evan
 */
public class PasswordSpec {
    private final int saltLength = 16;    
    private final int encodedSaltLength = 24;    
    private String algorithm;
    private int iterationCount;
    private int keySize;

    public PasswordSpec(int iterationCount, int keySize) {
        this("PBKDF2WithHmacSHA1", iterationCount, keySize);
    }
    
    public PasswordSpec(String algorithm, int iterationCount, int keySize) {
        this.algorithm = algorithm;
        this.iterationCount = iterationCount;
        this.keySize = keySize;
    }

    public byte[] hashPassword(char[] password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterationCount, keySize);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
            return factory.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }    
    
    public byte[] nextSalt() {
        byte[] salt = new byte[saltLength];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    public int getEncodedSaltLength() {
        return encodedSaltLength;
    }          
}















