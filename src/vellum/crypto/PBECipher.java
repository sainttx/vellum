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

/**
 *
 * @author evan
 */
public class PBECipher {
    private final int iterationCount = 2<<16;
    private final int keySize = 256;
    private Cipher encipher; 
    private Cipher decipher; 
    
    public PBECipher(char[] password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterationCount, keySize);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey secret = factory.generateSecret(spec);
        SecretKey aesSecret = new SecretKeySpec(secret.getEncoded(), "AES");
        encipher = Cipher.getInstance("AES/CBC/PKCS5Padding");        
        encipher.init(Cipher.ENCRYPT_MODE, aesSecret);
        AlgorithmParameters params = encipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();        
        decipher = Cipher.getInstance("AES/CBC/PKCS5Padding");        
        decipher.init(Cipher.DECRYPT_MODE, aesSecret, new IvParameterSpec(iv));        
    }

    public byte[] encrypt(byte[] bytes) throws Exception {
        return encipher.doFinal(bytes);
    }

    public byte[] decrypt(byte[] bytes) throws Exception {
        return decipher.doFinal(bytes);
    }    
}



