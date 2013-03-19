/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellumdemo.enigmademo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.Callable;
import javax.net.ssl.*;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class SimpleSSLClientDemo {

    static Logr logger = LogrFactory.getLogger(SimpleSSLClientDemo.class);
    static final String keyStorePath = System.getProperty("javax.net.ssl.keyStore");
    static final char[] keyStorePassword = System.getProperty("javax.net.ssl.keyStorePassword").toCharArray();
    static final char[] keyPassword = System.getProperty("javax.net.ssl.keyStorePassword").toCharArray();
    static final String trustStorePath = System.getProperty("javax.net.ssl.trustStore");
    static final char[] trustStorePassword = System.getProperty("javax.net.ssl.trustStorePassword").toCharArray();
    
    KeyManager[] keyManagers;
    TrustManager[] trustManagers;
    SecureRandom secureRandom;
    KeyStore keyStore;
    SSLContext sslContext;
    SSLSocket clientSocket;

    public SimpleSSLClientDemo() {
    }

    public void init() throws Exception {
        initKeyManagers();
        initTrustManagers();
        initSSLContext();
    }

    public void connect(String host, int port) throws Exception {
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        clientSocket = (SSLSocket) sslSocketFactory.createSocket(host, port);
        inspectCertificates();
    }

    protected void inspectCertificates() throws IOException {
        clientSocket.startHandshake();
        X509Certificate[] serverCertificates = (X509Certificate[]) 
                clientSocket.getSession().getPeerCertificates();
        for (X509Certificate certificate : serverCertificates) {
            logger.info(certificate.getIssuerDN().toString());
        }
    }

    protected void initKeyManagers() throws Exception {
        this.keyStore = KeyStore.getInstance("JKS");
        InputStream inputStream = new FileInputStream(keyStorePath);
        keyStore.load(inputStream, keyStorePassword);
        KeyManagerFactory keyManagerFactory = 
                KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, null);
        this.keyManagers = keyManagerFactory.getKeyManagers();
    }

    protected void initTrustManagers() throws Exception {
        TrustManagerFactory trustManagerFactory =
                TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(keyStore);
        this.trustManagers = trustManagerFactory.getTrustManagers();
    }

    protected void initSSLContext() throws Exception {
        sslContext = SSLContext.getInstance("SSL");
        sslContext.init(keyManagers, trustManagers, secureRandom);
    }

    protected void start() throws Exception {
        init();
        connect("google.com", 443);
    }
    
    public static void main(String[] args) throws Exception {
        try {
            new SimpleSSLClientDemo().start();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
    
}
