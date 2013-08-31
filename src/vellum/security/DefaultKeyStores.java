/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 * 
 */
package vellum.security;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.*;

/**
 *
 * @author evan.summers
 */
public class DefaultKeyStores {
    static final String keyStoreLocation = System.getProperty("javax.net.ssl.keyStore");
    static final char[] keyStorePassword = System.getProperty("javax.net.ssl.keyStorePassword").toCharArray();
    static final char[] keyPassword = System.getProperty("javax.net.ssl.keyStorePassword").toCharArray();
    static final String trustStoreLocation = System.getProperty("javax.net.ssl.trustStore");
    static final char[] trustStorePassword = System.getProperty("javax.net.ssl.trustStorePassword").toCharArray();
    public static final KeyStore keyStore = KeyStores.loadKeyStore("JKS", keyStoreLocation, keyStorePassword);
    public static final KeyStore trustStore = KeyStores.loadKeyStore("JKS", trustStoreLocation, trustStorePassword);
    public static final KeyManagerFactory keyManagerFactory = KeyStores.loadKeyManagerFactory(keyStore, keyStorePassword);
    public static final TrustManagerFactory trustManagerFactory = KeyStores.loadTrustManagerFactory(trustStore);

    public static PrivateKey getPrivateKey(String alias) throws Exception {
        return (PrivateKey) keyStore.getKey(alias, keyPassword);
    }

    public static X509Certificate getCert(String alias) throws Exception {
        return (X509Certificate) keyStore.getCertificate(alias);
    }

    public static X509TrustManager loadTrustManager() throws Exception {
        for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        throw new RuntimeException();
    }

    public static SSLSocketFactory createSSLSocketFactory() throws Exception {
        return createSSLContext().getSocketFactory();
    }
    
    public static SSLContext createSSLContext() throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        return sslContext;
    }

    public static SSLContext createSSLContext(KeyManagerFactory keyManagerFactory, TrustManagerFactory trustManagerFactory) throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        return sslContext;
    }
    
    public static SSLContext createSSLContext(TrustManager trustManager) throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = new TrustManager[]{trustManager};
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagers, new SecureRandom());
        return sslContext;
    }
    
}
