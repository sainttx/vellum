/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;

/**
 *
 * @author evan
 */
public class VCipherContext {
    Logr logger = LogrFactory.getLogger(getClass());
    VCipherProperties properties;    
    SecureRandom sr = new SecureRandom();
    SSLContext sslContext;
    InetSocketAddress address;
    InetAddress inetAddress;
    
    public VCipherContext() {
    }

    public void config(VCipherProperties properties, char[] keyStorePassword, char[] keyPassword, char[] trustStorePassword) throws Exception {
        this.properties = properties;        
        inetAddress = InetAddress.getByName(properties.serverIp);
        address = new InetSocketAddress(inetAddress, properties.sslPort);
        sslContext = SSLContext.getInstance("TLS");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        KeyStore ks = KeyStore.getInstance("JKS");
        File keyStoreFile = new File(properties.keyStore);
        logger.info(keyStoreFile.getAbsolutePath());
        FileInputStream fis = new FileInputStream(keyStoreFile);
        ks.load(fis, keyStorePassword);
        kmf.init(ks, keyPassword);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        KeyStore ts = KeyStore.getInstance("JKS");
        FileInputStream trustStoreStream = new FileInputStream(properties.trustStore);
        ts.load(trustStoreStream, trustStorePassword);
        tmf.init(ts);
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), sr);
    }
}
