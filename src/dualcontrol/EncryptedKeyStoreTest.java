/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */
package dualcontrol;

import java.util.Arrays;
import javax.crypto.SecretKey;
import org.apache.log4j.Logger;
import vellum.crypto.Encrypted;
import vellum.crypto.VellumCipher;
import vellum.datatype.Millis;

/**
 *
 * @author evans
 */
public class EncryptedKeyStoreTest {
    private final static Logger logger = Logger.getLogger(EncryptedKeyStoreTest.class);
    private final static String keyAlg = "AES";
    private final static int keySize = 128;
    private String keyStoreLocation;
    private String keyStoreType;
    private String keyAlias;
    private char[] keyPass;

    public static void main(String[] args) throws Exception {
        logger.debug("main " + Arrays.toString(args));
        if (args.length != 4) {
            System.err.println("usage: keystore storetype alias keyPass"); 
        } else {
            new EncryptedKeyStoreTest(args[0], args[1], args[2], args[3].toCharArray());
        }
    }    
    
    public EncryptedKeyStoreTest(String keyStoreLocation, String keyStoreType,  
            String alias, char[] keyPass) {
        this.keyStoreLocation = keyStoreLocation;
        this.keyStoreType = keyStoreType;
        this.keyAlias = alias;
        this.keyPass = keyPass;
    }
    
    private void start() throws Exception {
        String data = "4000555500001111";
        SecretKey dek = AESCiphers.generateKey(keySize);
        VellumCipher cipher = AESCiphers.getCipher(dek);
        Encrypted encrypted = cipher.encrypt(data.getBytes());
        logger.info(cipher.decrypt(encrypted));
        long millis = System.currentTimeMillis();
        EncryptedKeyStores.storeKey(dek, keyStoreLocation, 
                keyStoreType, keyAlias, keyPass);
        Log.infof(logger, "store %dms", Millis.elapsed(millis));
        millis = System.currentTimeMillis();
        dek = EncryptedKeyStores.loadKey(keyStoreLocation, 
                keyStoreType, keyAlias, keyPass);
        Log.infof(logger, "load %dms", Millis.elapsed(millis));
        Log.info(logger, keyAlias, dek.getAlgorithm());        
    }
}
