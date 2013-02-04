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
    KeyStore keyStore;

    public MantraKeyStoreManager() {        
    }
    public void loadKeyStore(String keyStorePath, char[] keyStorePassword) {
        keyStore = KeyStores.loadKeyStore("JKS", keyStorePath, keyStorePassword);
    }
    
    public PrivateKey getPrivateKey(String alias, char[] keyPassword) throws Exception {
        return (PrivateKey) keyStore.getKey(alias, keyPassword);
    }

    public X509Certificate getCert(String alias) throws Exception {
        return (X509Certificate) keyStore.getCertificate(alias);
    }

    
}
