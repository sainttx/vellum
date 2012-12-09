/*
 * Copyright Evan Summers
 * 
 */
package saltserver.crypto;

import vellum.crypto.*;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.KeyStore;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author evan
 */
public class AESCipher {
    private final SecretKey aesSecret;
    
    public AESCipher(KeyStore keyStore, String alias, char[] password) throws Exception {
        Key secret = keyStore.getKey(alias, password);
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
