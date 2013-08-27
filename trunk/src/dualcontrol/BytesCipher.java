/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */
package dualcontrol;

import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import vellum.crypto.Encrypted;
import vellum.crypto.VellumCipher;

/**
 *
 * @author evans
 */
public class BytesCipher implements VellumCipher {
    private String cipherTransform;    
    private Key key;
    
    BytesCipher(Key key, String cipherTransform) {
        this.key = key;
        this.cipherTransform = cipherTransform;
    }
    
    public Encrypted encrypt(byte[] bytes) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(cipherTransform);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        return new Encrypted(iv, cipher.doFinal(bytes));
    }

    @Override
    public byte[] encrypt(byte[] bytes, byte[] iv) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(cipherTransform);
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        return cipher.doFinal(bytes);
    }

    @Override
    public byte[] decrypt(Encrypted encrypted) throws GeneralSecurityException {
        return decrypt(encrypted.getEncryptedBytes(), encrypted.getIv());
    }

    @Override
    public byte[] decrypt(byte[] bytes, byte[] iv) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(cipherTransform);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        return cipher.doFinal(bytes);
    }
}
