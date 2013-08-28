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
import javax.net.ssl.SSLContext;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class DualControlGenSecKey {
    final static Logger logger = Logger.getLogger(DualControlGenSecKey.class);
    private int submissionCount;
    private String keyAlias;
    private String keyStoreLocation;
    private String keyStoreType;
    private String keyAlg;
    private int keySize;
    private SSLContext sslContext;

    public static void main(String[] args) throws Exception {
        logger.info("main " + Arrays.toString(args));
        try {
            new DualControlGenSecKey().call(VellumProperties.systemProperties);
        } catch (DualControlException e) {
            logger.error(e.getMessage());
        }
    }

    public KeyStore call(VellumProperties properties) throws Exception {
        submissionCount = properties.getInt("dualcontrol.submissions", 3);
        keyAlias = properties.getString("alias");
        keyStoreLocation = properties.getString("keystore");
        keyStoreType = properties.getString("storetype");
        keyAlg = properties.getString("keyalg");
        keySize = properties.getInt("keysize");
        sslContext = DualControlSSLContextFactory.createSSLContext(properties);
        return call();
    }
    
    private KeyStore call() throws Exception {
        String purpose = "new key " + keyAlias;
        Map<String, char[]> dualMap = new DualControlReader(sslContext).
                readDualMap(purpose, submissionCount);
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
