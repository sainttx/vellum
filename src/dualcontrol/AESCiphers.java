/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */
package dualcontrol;

import java.security.GeneralSecurityException;
import java.security.Key;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 *
 * @author evans
 */
public class AESCiphers {
    private static String keyAlg = "AES";
    private static final String cipherTransform = "AES/CBC/PKCS5Padding";    
    
    public static SecretKey generateKey(int keySize) 
            throws GeneralSecurityException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(keyAlg);
        keyGenerator.init(keySize);
        return keyGenerator.generateKey();        
    }
    
    public static BytesCipher getCipher(Key key) {
        return new BytesCipher(key, cipherTransform);
    }
    
}
