/*
 * Copyright Evan Summers
 * 
 */
package venigma.server;

import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;

/**
 *
 * @author evan
 */
public class Ciphers {
    static Logr logger = LogrFactory.getLogger(Ciphers.class);    

    public static Key loadKey(String keyStoreFile, String keyAlias, char[] storePass, char[] keyPass) throws Exception {
        File file = new File(keyStoreFile);
        KeyStore keyStore = KeyStore.getInstance("JCEKS", "VProvider");
        keyStore.load(new FileInputStream(file), storePass);
        logger.info("loadKey", keyStore.getType(), keyStore.getProvider().getName());
        Key key = keyStore.getKey(keyAlias, keyPass);
        logger.info(key.getAlgorithm(), key.getFormat());
        return key;
    }
        
    
    
    
}
