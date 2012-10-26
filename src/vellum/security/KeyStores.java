/*
 * Copyright Evan Summers
 * 
 */
package vellum.security;

import vellum.util.*;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.*;
import javax.net.ssl.*;
import vellum.exception.Exceptions;

/**
 *
 * @author evan
 */
public class KeyStores {

    public static SSLSocketFactory createSSLSocketFactory() throws Exception {
        return createSSLContext().getSocketFactory();
    }

    public static KeyManagerFactory loadKeyManagerFactory(KeyStore keyStore, char[] password) throws Exception {
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, password);
        return kmf;

    }

    public static TrustManagerFactory loadTrustManagerFactory() throws Exception {
        char[] password = System.getProperty("javax.net.ssl.trustStorePassword").toCharArray();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        KeyStore ts = loadKeyStore("JKS", System.getProperty("javax.net.ssl.trustStore"), password);
        tmf.init(ts);
        return tmf;
    }

    public static X509TrustManager loadTrustManager() throws Exception {
        char[] password = System.getProperty("javax.net.ssl.trustStorePassword").toCharArray();
        KeyStore ts = loadKeyStore("JKS", System.getProperty("javax.net.ssl.trustStore"), password);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ts);
        for (TrustManager trustManager : tmf.getTrustManagers()) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        throw new RuntimeException();
    }

    public static KeyStore loadKeyStore(String type, String filePath, char[] password) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(type);
        FileInputStream inputStream = new FileInputStream(filePath);
        keyStore.load(inputStream, password);
        return keyStore;
    }

    public static SSLContext createSSLContext() throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        char[] password = System.getProperty("javax.net.ssl.keyStorePassword").toCharArray();
        KeyStore ks = loadKeyStore("JKS", System.getProperty("javax.net.ssl.keyStore"), password);
        KeyManagerFactory kmf = loadKeyManagerFactory(ks, password);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        KeyStore ts = loadKeyStore("JKS", System.getProperty("javax.net.ssl.trustStore"), password);
        tmf.init(ts);
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
        return sslContext;
    }

    public static SSLContext createSSLContext(TrustManager trustManager) throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        char[] password = System.getProperty("javax.net.ssl.keyStorePassword").toCharArray();
        KeyStore ks = loadKeyStore("JKS", System.getProperty("javax.net.ssl.keyStore"), password);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, password);
        TrustManager[] trustManagers = new TrustManager[]{trustManager};
        sslContext.init(kmf.getKeyManagers(), trustManagers, new SecureRandom());
        return sslContext;
    }

    public static HttpsConfigurator createHttpsConfigurator(SSLContext sslContext, final boolean needClientAuth) throws Exception {
        return new HttpsConfigurator(sslContext) {

            @Override
            public void configure(HttpsParameters httpsParameters) {
                SSLContext sslContext = getSSLContext();
                InetSocketAddress remote = httpsParameters.getClientAddress();
                if (remote.getHostName().equals("localhost")) {
                }
                SSLParameters defaultSSLParameters = sslContext.getDefaultSSLParameters();
                defaultSSLParameters.setNeedClientAuth(needClientAuth);
                httpsParameters.setSSLParameters(defaultSSLParameters);
            }
        };
    }
}
