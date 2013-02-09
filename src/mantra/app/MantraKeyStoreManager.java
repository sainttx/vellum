/*
 * Copyright Evan Summers
 * 
 */
package mantra.app;

import vellum.security.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 *
 * @author evan
 */
public class MantraKeyStoreManager {
    String keyStorePath;
    KeyStore keyStore;

    public MantraKeyStoreManager(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }
    
    public void loadKeyStore(char[] keyStorePassword) {
        keyStore = KeyStores.loadKeyStore("JKS", keyStorePath, keyStorePassword);
    }
    
    public PrivateKey getPrivateKey(String alias, char[] keyPassword) throws Exception {
        return (PrivateKey) keyStore.getKey(alias, keyPassword);
    }

    public X509Certificate getCert(String alias) throws Exception {
        return (X509Certificate) keyStore.getCertificate(alias);
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }
    
    public void create(char[] password) throws Exception {
        KeyStores.createKeyStore("jks", keyStorePath, password);    
    }
    
    
}
