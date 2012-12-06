/*
 * Copyright Evan Summers
 * 
 */
package vellum.util;

import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author evan
 */
public class PasswordSpec {
    private String algorithm;
    private int iterationCount;
    private int keySize;

    public PasswordSpec(String algorithm, int iterationCount, int keySize) {
        this.algorithm = algorithm;
        this.iterationCount = iterationCount;
        this.keySize = keySize;
    }

    public String hashPassword(char[] password, byte[] salt) {
        try {
            KeySpec spec = new PBEKeySpec(password, salt, iterationCount, keySize);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.encode(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }    
}
