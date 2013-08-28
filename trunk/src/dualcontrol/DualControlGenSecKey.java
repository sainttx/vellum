/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */
package dualcontrol;

import java.io.FileOutputStream;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Map;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.apache.log4j.Logger;
import vellum.util.Args;

/**
 *
 * @author evans
 */
public class DualControlGenSecKey {
    final static Logger logger = Logger.getLogger(DualControlGenSecKey.class);
    private VellumProperties properties = VellumProperties.systemProperties;
    private int submissionCount = properties.getInt("dualcontrol.submissions", 3);
    private String keyAlias = properties.getString("alias");
    private String keyStoreLocation = properties.getString("keystore");
    private String keyStoreType = properties.getString("storetype");
    private String keyAlg = properties.getString("keyalg");
    private int keySize = properties.getInt("keysize");
    
    public static void main(String[] args) throws Exception {
        logger.info("main " + Arrays.toString(args));
        try {
            new DualControlGenSecKey().call();
        } catch (DualControlException e) {
            logger.error(e.getMessage());
        }
    }

    public KeyStore call() throws Exception {
        String purpose = "new key " + keyAlias;
        Map<String, char[]> dualMap = new DualControlReader(
                DualControlSSLContextFactory.createSSLContext(
                properties)).readDualMap(purpose, submissionCount);
        char[] keyStorePassword = DualControlKeyStoreTools.getKeyStorePassword();
        KeyGenerator keyGenerator = KeyGenerator.getInstance(keyAlg);
        keyGenerator.init(keySize);
        SecretKey secretKey = keyGenerator.generateKey();
        KeyStore keyStore = DualControlKeyStores.loadLocalKeyStore(keyStoreLocation, 
                keyStoreType, keyStorePassword);
        KeyStore.Entry entry = new KeyStore.SecretKeyEntry(secretKey);
        for (String dualAlias : dualMap.keySet()) {
            char[] dualPassword = dualMap.get(dualAlias);
            String alias = keyAlias + "-" + dualAlias;
            KeyStore.ProtectionParameter prot = 
                    new KeyStore.PasswordProtection(dualPassword);
            keyStore.setEntry(alias, entry, prot);
            logger.info("alias " + alias);
        }
        keyStore.store(new FileOutputStream(keyStoreLocation), keyStorePassword);
        return keyStore;
    }
}
