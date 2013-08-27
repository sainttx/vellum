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
    private int submissionCount = SystemProperties.getInt("dualcontrol.submissions", 3);
    private String keyAlias = SystemProperties.getString("alias");
    private String keyStoreLocation = SystemProperties.getString("keystore");
    private String keyStoreType = SystemProperties.getString("storetype");
    private String keyAlg = SystemProperties.getString("keyalg");
    private int keySize = SystemProperties.getInt("keysize");
    private char[] keyStorePassword;
    private Map<String, char[]> dualMap;
    private KeyStore keyStore;
    private SecretKey secretKey;

    public static void main(String[] args) throws Exception {
        logger.info("main " + Arrays.toString(args));
        try {
            new DualControlGenSecKey().start();
        } catch (DualControlException e) {
            logger.error(e.getMessage());
        }
    }

    void start() throws Exception {
        String purpose = "new key " + keyAlias;
        dualMap = new DualControlReader(DualControlSSLContextFactory.createSSLContext()).
                readDualMap(purpose, submissionCount);
        keyStorePassword = DualControlKeyStoreTools.getKeyStorePassword();
        KeyGenerator keyGenerator = KeyGenerator.getInstance(keyAlg);
        keyGenerator.init(keySize);
        secretKey = keyGenerator.generateKey();
        keyStore = DualControlKeyStores.loadLocalKeyStore(keyStoreLocation, 
                keyStoreType, keyStorePassword);
        KeyStore.Entry entry = new KeyStore.SecretKeyEntry(secretKey);
        for (String dualAlias : dualMap.keySet()) {
            char[] dualPassword = dualMap.get(dualAlias);
            String alias = keyAlias + "-" + dualAlias;
            if (true) {
                logger.info(Args.format(
                        new String(keyStorePassword), dualAlias, alias, 
                        dualPassword.length, new String(dualPassword)));
            }
            KeyStore.ProtectionParameter prot = 
                    new KeyStore.PasswordProtection(dualPassword);
            keyStore.setEntry(alias, entry, prot);
        }
        keyStore.store(new FileOutputStream(keyStoreLocation), keyStorePassword);
    }
}
