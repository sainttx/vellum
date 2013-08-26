/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */
package dualcontrol;

import java.io.FileOutputStream;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class DualControlEnroll {

    final static Logger logger = Logger.getLogger(DualControlEnroll.class);
    private int submissionCount = SystemProperties.getInt("dualcontrol.submissions", 3);
    private String username = SystemProperties.getString("dualcontrol.username");
    private String keyAlias = SystemProperties.getString("alias");
    private String keyStoreLocation = SystemProperties.getString("keystore");
    private String keyStoreType = SystemProperties.getString("storetype");
    private char[] keyStorePassword;
    private Map<String, char[]> dualMap;
    private KeyStore keyStore;
    private SecretKey secretKey;
    List<String> aliasList;

    public static String getStringProperty(String propertyName) {
        String propertyValue = System.getProperty("alias");
        if (propertyValue == null) {
            throw new RuntimeException("Missing -D property: " + propertyName);
        }
        return propertyValue;
    } 

    public static void main(String[] args) throws Exception {
        logger.info("main " + Arrays.toString(args));
        new DualControlEnroll().start();
    }

    void start() throws Exception {
        String purpose = String.format("key %s to enroll %s", keyAlias, username);
        dualMap = new DualControlReader().readDualMap(purpose, submissionCount);
        keyStorePassword = DualControlKeyStoreTools.getKeyStorePassword();
        keyStore = DualControlKeyStores.loadLocalKeyStore(keyStoreLocation, 
                keyStoreType, keyStorePassword);
        aliasList = Collections.list(keyStore.aliases());
        secretKey = getKey();
        KeyStore.Entry entry = new KeyStore.SecretKeyEntry(secretKey);
        for (String dualAlias : dualMap.keySet()) {
            char[] dualPassword = dualMap.get(dualAlias);
            String alias = keyAlias + "-" + dualAlias;
            if (!aliasList.contains(alias)) {
                KeyStore.ProtectionParameter prot = 
                        new KeyStore.PasswordProtection(dualPassword);
                keyStore.setEntry(alias, entry, prot);
            }
        }
        keyStore.store(new FileOutputStream(keyStoreLocation), keyStorePassword);
    }
    
    SecretKey getKey() throws Exception {
        for (String alias : aliasList) {
            logger.debug("existing alias " + alias);
        }
        for (String dualAlias : dualMap.keySet()) {
            char[] dualPassword = dualMap.get(dualAlias);
            String alias = keyAlias + "-" + dualAlias;
            logger.debug("try " + alias);
            if (aliasList.contains(alias)) {
                return (SecretKey) keyStore.getKey(alias, dualPassword);
            }
        }      
        throw new Exception("Key not found");
    }
}
