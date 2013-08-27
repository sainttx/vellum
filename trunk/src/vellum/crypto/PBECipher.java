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
public final class PBECipher implements VellumCipher {
    private final String KEY_FACTORY = "PBKDF2WithHmacSHA1";
    private final String KEY_ALG = "AES";
    private final String CIPHER_TRANS = "AES/CBC/PKCS5Padding";
    
    private final SecretKey pbeKey;

    public PBECipher(char[] password, PasswordHash hash) throws GeneralSecurityException  {
        this(password, hash.getSalt(), hash.getIterationCount(), hash.getKeySize());
        assert hash.matches(password);
    }
    
    public PBECipher(char[] password, byte[] salt, int iterationCount, int keySize) 
            throws GeneralSecurityException  {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterationCount, keySize);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_FACTORY);
        SecretKey secret = factory.generateSecret(spec);
        pbeKey = new SecretKeySpec(secret.getEncoded(), KEY_ALG);
    }

    @Override
    public Encrypted encrypt(byte[] bytes) throws GeneralSecurityException  {
        Cipher cipher = Cipher.getInstance(CIPHER_TRANS);
        cipher.init(Cipher.ENCRYPT_MODE, pbeKey);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        return new Encrypted(iv, cipher.doFinal(bytes));
    }

    @Override
    public byte[] decrypt(byte[] bytes, byte[] iv) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(CIPHER_TRANS);
        cipher.init(Cipher.DECRYPT_MODE, pbeKey, new IvParameterSpec(iv));
        return cipher.doFinal(bytes);
    }
    
    @Override
    public byte[] encrypt(byte[] bytes, byte[] iv) throws GeneralSecurityException  {
        Cipher cipher = Cipher.getInstance(CIPHER_TRANS);
        cipher.init(Cipher.ENCRYPT_MODE, pbeKey, new IvParameterSpec(iv));
        return cipher.doFinal(bytes);
    }            
}
