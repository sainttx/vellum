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
import vellum.crypto.VellumCipher;

/**
 *
 * @author evans
 */
public class CipheredKeyStore {

    public static SecretKey loadKey(String keyStoreLocation, String keyStoreType, 
            String alias, char[] pass, VellumCipher cipher, byte[] iv) throws Exception {
        File file = new File(keyStoreLocation);
        if (!file.exists()) {
            throw new Exception("Encrypted keystore file not found: " + keyStoreLocation);
        }
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fis.read(bytes);
        bytes = cipher.decrypt(bytes, iv);
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);        
        keyStore.load(stream, pass);
        return (SecretKey) keyStore.getKey(alias, pass);
    }

    public static void storeKey(SecretKey secretKey, String keyStoreLocation, String keyStoreType, 
            String alias, char[] pass, VellumCipher cipher, byte[] iv) throws Exception {
        File file = new File(keyStoreLocation);
        if (!file.exists()) {
            throw new Exception("Encrypted keystore file already exists: " + keyStoreLocation);
        }
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        KeyStore.Entry entry = new KeyStore.SecretKeyEntry(secretKey);
        KeyStore.ProtectionParameter prot = new KeyStore.PasswordProtection(pass);
        keyStore.setEntry(alias, entry, prot);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        keyStore.store(os, pass);
        byte[] bytes = cipher.encrypt(os.toByteArray(), iv);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
    }    
}
