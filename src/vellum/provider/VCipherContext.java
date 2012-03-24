/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 * @author evan
 */
public class VCipherContext {
    VCipherProperties properties;    
    SSLContext sslContext;
    InetSocketAddress address;
    InetAddress inetAddress;
    
    public VCipherContext() {
    }

    private void start(VCipherProperties properties) throws Exception {
        this.properties = properties;        
        inetAddress = InetAddress.getByName(properties.serverIp);
        address = new InetSocketAddress(inetAddress, properties.sslPort);
        init();
        new VCipherServer().start(this);
    }
    
    private void init() throws Exception {
        sslContext = SSLContext.getInstance("TLS");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        KeyStore ks = KeyStore.getInstance("JKS");
        FileInputStream fis = new FileInputStream(properties.keyStore);
        char[] keyStorePassword = properties.keyStorePassword;
        char[] keyPassword = properties.keyPassword;
        ks.load(fis, keyStorePassword);
        kmf.init(ks, keyPassword);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        KeyStore ts = KeyStore.getInstance("JKS");
        FileInputStream trustStoreStream = new FileInputStream(properties.trustStore);
        ts.load(trustStoreStream, keyStorePassword);
        tmf.init(ts);
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());                
    }
    
    public static void main(String[] args) {
        try {
            VCipherProperties properties = new VCipherProperties();
            new VCipherContext().start(properties);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
    
    
}
