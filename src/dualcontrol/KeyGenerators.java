/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */
package dualcontrol;

import java.security.GeneralSecurityException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 *
 * @author evans
 */
public class KeyGenerators {

    public static SecretKey generateKey(String keyAlg, int keySize) 
            throws GeneralSecurityException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(keyAlg);
        keyGenerator.init(keySize);
        return keyGenerator.generateKey();        
    }
}
