/*
 * Copyright Evan Summers
 * 
 */
package vellum.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author evan
 */
public class SaltCipher {
    private final int iterationCount = 65536;
    private final int keySize = 256;
    private byte[] salt = Base64.decode("nD++3Wv9h9MqnS3bO3KJzA==");
    
    public SaltCipher() {
    }
    
    public Cipher createCipher(int mode, char[] password) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterationCount, keySize);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey secret = factory.generateSecret(spec);
        SecretKey aesSecret = new SecretKeySpec(secret.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, aesSecret);        
        return cipher;
    }    
}















