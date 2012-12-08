/*
 * Copyright Evan Summers
 * 
 */
package vellum.crypto;

import vellum.crypto.Base64;
import java.security.AlgorithmParameters;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class PBECipher {
    private final int iterationCount = 2<<16;
    private final int keySize = 256;
    private final SecretKey aesSecret;
    
    public PBECipher(char[] password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterationCount, keySize);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey secret = factory.generateSecret(spec);
        aesSecret = new SecretKeySpec(secret.getEncoded(), "AES");
    }

    public Encrypted encrypt(byte[] bytes) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, aesSecret);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        return new Encrypted(iv, cipher.doFinal(bytes));
    }

    public byte[] decrypt(byte[] bytes, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, aesSecret, new IvParameterSpec(iv));
        return cipher.doFinal(bytes);
    }    
    
    public byte[] encrypt(byte[] bytes, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, aesSecret, new IvParameterSpec(iv));
        return cipher.doFinal(bytes);
    }
        
    
}
