/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
 * 
 */
package vellum.security;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.X509Certificate;
import javax.net.ssl.*;
import vellum.exception.Exceptions;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * snippets from OpenJDK7 KeyTool etc.
 *
 * @author evan.summers
 */
public class KeyStores {

    static Logr logger = LogrFactory.getLogger(KeyStores.class);

    public static X509TrustManager loadTrustManager(TrustManagerFactory trustManagerFactory) throws Exception {
        for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        throw new RuntimeException();
    }
    
    public static SSLSocketFactory createSSLSocketFactory(String keyStoreLocation, 
            String keyStoreType, char[] keyStorePassword, char[] keyPassword,
            String trustStoreLocation, char[] trustStorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(new FileInputStream(keyStoreLocation), keyStorePassword);
        KeyStore trustStore = loadKeyStore("JKS", trustStoreLocation, trustStorePassword);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, keyPassword);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(trustStore);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), 
                trustManagerFactory.getTrustManagers(), new SecureRandom());
        return sslContext.getSocketFactory();
    }
    
    public static SSLContext createSSLContext(KeyManagerFactory keyManagerFactory, 
            TrustManagerFactory trustManagerFactory) throws Exception {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        return sslContext;
    }
    
    public static TrustManagerFactory loadTrustManagerFactory(KeyStore trustStore) {
        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(trustStore);
            return tmf;
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
    }

    public static KeyManagerFactory loadKeyManagerFactory(KeyStore keyStore, char[] keyPassword) {
        try {
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, keyPassword);
            return kmf;
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
    }

    public static KeyStore loadKeyStore(String type, String filePath, char[] keyStorePassword) {
        try {
            KeyStore keyStore = KeyStore.getInstance(type);
            FileInputStream inputStream = new FileInputStream(filePath);
            keyStore.load(inputStream, keyStorePassword);
            return keyStore;
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
    }

    
    public static HttpsConfigurator createHttpsConfigurator(
            SSLContext sslContext, final boolean needClientAuth) throws Exception {
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

    public static void createKeyStore(String type, String fileName, char[] password) throws Exception {
        KeyStore ks = KeyStore.getInstance(type);
        ks.load(null, password);
        FileOutputStream fos = new FileOutputStream(fileName);
        ks.store(fos, password);
        fos.close();
    }
    
    public static X509Certificate findRootCert(KeyStore keyStore, String alias) throws Exception {
        return Certificates.findRootCert(keyStore.getCertificateChain(alias));
    }    
}
