/*
 * Copyright Evan Summers
 * 
 */
package vellum.crypto;

import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.util.Arrays;
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
public final class PBECipher {
    private final SecretKey key;

    public PBECipher(char[] password, PasswordHash salt) throws GeneralSecurityException  {
        this(password, salt.getSalt(), salt.getIterationCount(), salt.getKeySize());
        if (!Arrays.equals(salt.getSalt(), decrypt(salt.getHash(), salt.getIv()))) {
            throw new IllegalArgumentException("PBE password is incorrect");
        }
    }
    
    public PBECipher(char[] password, byte[] salt, int iterationCount, int keySize) 
            throws GeneralSecurityException  {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterationCount, keySize);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey secret = factory.generateSecret(spec);
        key = new SecretKeySpec(secret.getEncoded(), "AES");
    }

    public Encrypted encrypt(byte[] bytes) throws GeneralSecurityException  {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        return new Encrypted(iv, cipher.doFinal(bytes));
    }

    public byte[] decrypt(byte[] bytes, byte[] iv) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        return cipher.doFinal(bytes);
    }
    
    public byte[] encrypt(byte[] bytes, byte[] iv) throws GeneralSecurityException  {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        return cipher.doFinal(bytes);
    }            
}
