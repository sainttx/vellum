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
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class DualControlRevoke {

    final static Logger logger = Logger.getLogger(DualControlRevoke.class);
    private VellumProperties properties = VellumProperties.systemProperties;
    private String username = properties.getString("dualcontrol.username");
    private String keyAlias = properties.getString("alias");
    private String keyStoreLocation = properties.getString("keystore");
    private String keyStoreType = properties.getString("storetype");
    private char[] keyStorePassword;
    private KeyStore keyStore;
    List<String> aliasList;

    public static void main(String[] args) throws Exception {
        logger.info("main " + Arrays.toString(args));
        try {
            new DualControlRevoke().start();
        } catch (DualControlException e) {
            logger.error(e.getMessage());
        }
    }

    void start() throws Exception {
        keyStorePassword = DualControlKeyStoreTools.getKeyStorePassword();
        keyStore = DualControlKeyStores.loadLocalKeyStore(keyStoreLocation, 
                keyStoreType, keyStorePassword);
        aliasList = Collections.list(keyStore.aliases());
        for (String alias : aliasList) {
            logger.debug("alias " + alias);
            if (matches(alias)) {
                logger.info("delete " + alias);
                keyStore.deleteEntry(alias);
            }
        }
        keyStore.store(new FileOutputStream(keyStoreLocation), keyStorePassword);
    }

    boolean matches(String alias) {
        if (alias.startsWith(keyAlias + "-" + username + "-")) {
            return true;
        }
        if (alias.startsWith(keyAlias + "-") && alias.endsWith("-" + username)) {
            return true;
        }
        return false;
    }
}
