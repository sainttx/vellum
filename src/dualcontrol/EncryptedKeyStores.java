/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */
package dualcontrol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import javax.crypto.SecretKey;

/**
 *
 * @author evans
 */
public class EncryptedKeyStores {
    
    public static void storeKey(SecretKey secretKey, String keyStoreLocation, String keyStoreType, 
            String alias, char[] password) throws Exception {
        File file = new File(keyStoreLocation);
        if (!file.exists()) {
            throw new Exception("Encrypted keystore file already exists: " + keyStoreLocation);
        }
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        KeyStore.Entry entry = new KeyStore.SecretKeyEntry(secretKey);
        KeyStore.ProtectionParameter prot = new KeyStore.PasswordProtection(password);
        keyStore.setEntry(alias, entry, prot);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        keyStore.store(baos, password);
        new EncryptedStore().store(new FileOutputStream(file), keyStoreType, 
                alias, baos.toByteArray(), password);
    }
    
    public static SecretKey loadKey(String keyStoreLocation, String keyStoreType, 
            String alias, char[] password) throws Exception {
        File file = new File(keyStoreLocation);
        if (!file.exists()) {
            throw new Exception("Encrypted keystore file not found: " + keyStoreLocation);
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(
                new EncryptedStore().load(new FileInputStream(file), keyStoreType, 
                alias, password));
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);        
        keyStore.load(bais, password);
        return (SecretKey) keyStore.getKey(alias, password);
    }
}
