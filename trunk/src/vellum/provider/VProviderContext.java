/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.*;

/**
 *
 * @author evan
 */
public class VProviderContext {
    VProviderProperties properties;
    KeyStore keyStore;
    SSLContext sslContext;
    KeyManager[] keyManagers;
    TrustManager[] trustManagers;
    SecureRandom secureRandom;

    public VProviderContext(VProviderProperties properties) {
        this.properties = properties;
    }
    
    public void init() throws Exception {
        initKeyManagers();
        initTrustManagers();
        initSSLContext();
    }
    
    private void initKeyManagers() throws Exception {
        keyStore = KeyStore.getInstance("JKS");
        InputStream inputStream = new FileInputStream(properties.keyStore);
        keyStore.load(inputStream, properties.keyStorePassword);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, properties.keyPassword);
        this.keyManagers = keyManagerFactory.getKeyManagers();
    }

    private void initTrustManagers() throws Exception {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(keyStore);        
        this.trustManagers = trustManagerFactory.getTrustManagers();
    }
    
    private void initSSLContext() throws Exception {
        sslContext = SSLContext.getInstance("SSL");
        sslContext.init(keyManagers, trustManagers, secureRandom);
    }

    public SSLContext getSSLContext() {
        return sslContext;
    }    
}
