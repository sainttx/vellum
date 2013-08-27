/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */
package dualcontrol;

import java.util.Arrays;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class EncryptedKeyStoreTest {
    private final static Logger logger = Logger.getLogger(EncryptedKeyStoreTest.class);
    private String keyStoreLocation;
    private String keyStoreType;
    private String keyAlias;
    private char[] keyPass;
    private String keyAlg;
    private int keySize;

    public static void main(String[] args) throws Exception {
        logger.debug("main " + Arrays.toString(args));
        if (args.length != 6) {
            System.err.println("usage: keystore storetype alias keyPass alg kz"); 
        } else {
            new EncryptedKeyStoreTest(args[0], args[1], args[3], args[2].toCharArray(),
                    args[4], Integer.parseInt(args[5])).start();
        }
    }    
    
    public EncryptedKeyStoreTest(String keyStoreLocation, String keyStoreType,  
            String alias, char[] keyPass, String keyAlg, int keySize) {
        this.keyStoreLocation = keyStoreLocation;
        this.keyStoreType = keyStoreType;
        this.keyAlias = alias;
        this.keyPass = keyPass;
        this.keyAlg = keyAlg;
        this.keySize = keySize;
    }
    
    private void start() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(keyAlg);
        keyGenerator.init(keySize);
        SecretKey dek = keyGenerator.generateKey();
        EncryptedKeyStores.storeKey(dek, keyStoreLocation, 
                keyStoreType, keyAlias, keyPass);
        dek = EncryptedKeyStores.loadKey(keyStoreLocation, 
                keyStoreType, keyAlias, keyPass);
        Log.info(logger, keyAlias, dek.getAlgorithm());        
    }    
}
