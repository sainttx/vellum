/*
 * Copyright Evan Summers
 * 
 */
package vellum.security;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.*;
import sun.misc.BASE64Encoder;
import sun.security.provider.X509Factory;
import sun.security.x509.CertAndKeyGen;
import sun.security.x509.X500Name;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class KeyStores {
    static Logr logger = LogrFactory.getLogger(KeyStores.class);
    public static final String BEGIN_PRIVATE_KEY = X509Factory.BEGIN_CERT.replace("BEGIN CERT", "BEGIN PRIVATE KEY");
    public static final String END_PRIVATE_KEY = X509Factory.BEGIN_CERT.replace("END CERT", "END PRIVATE KEY");
    public static final String LOCAL_DNAME = "CN=localhost, OU=local, O=local, L=local, S=local, C=local";

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
    
    public static String buildPrivateKeyPem(PrivateKey privateKey) throws Exception, CertificateException {
        StringBuilder builder = new StringBuilder();
        BASE64Encoder encoder = new BASE64Encoder();
        builder.append(BEGIN_PRIVATE_KEY);
        builder.append(encoder.encodeBuffer(privateKey.getEncoded()));
        builder.append(END_PRIVATE_KEY);
        return builder.toString();
    }
    
    public static String buildCertPem(X509Certificate cert) throws Exception, CertificateException {
        StringBuilder builder = new StringBuilder();
        builder.append(X509Factory.BEGIN_CERT);
        BASE64Encoder encoder = new BASE64Encoder();
        builder.append(encoder.encodeBuffer(cert.getEncoded()));
        builder.append(X509Factory.END_CERT);
        return builder.toString();
    }

    public static String formatDname(String cn, String ou, String o, String l, String s, String c) throws IOException {
        X500Name name = new X500Name(cn, ou, o, l, s, c);
        String dname = name.toString();
        logger.info(dname);
        return dname;
    }
        
}
