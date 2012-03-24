/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.*;

/**
 *
 * @author evan
 */
public class VProviderContext {
    public static final String CHARSET = "UTF8";
    public static VProviderContext instance = new VProviderContext();
    
    VProviderProperties properties;
    char[] keyStorePassword;
    char[] keyPassword;
    InetAddress serverInetAddress;
    InetSocketAddress serverSocketAddress;
    
    KeyStore keyStore;
    SSLContext sslContext;
    KeyManager[] keyManagers;
    TrustManager[] trustManagers;
    SecureRandom secureRandom;

    private VProviderContext() {        
    }
    
    public void config(VProviderProperties properties, char[] keyStorePassword, char[] keyPassword) throws Exception {
        this.properties = properties;
        this.keyStorePassword = keyStorePassword;        
        this.keyPassword = keyPassword;        
        this.serverInetAddress = InetAddress.getByName(properties.serverIp);
        this.serverSocketAddress = new InetSocketAddress(serverInetAddress, properties.sslPort);
        initKeyManagers();
        initTrustManagers();
        initSSLContext();
    }
    
    private void initKeyManagers() throws Exception {
        keyStore = KeyStore.getInstance("JKS");
        InputStream inputStream = new FileInputStream(properties.keyStore);
        keyStore.load(inputStream, keyStorePassword);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, keyPassword);
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
    
    public char[] getKeyStorePassword() {
        return keyStorePassword;
    }

    public VCipherConnection newConnection() {
        return new VCipherConnection();
    }

    public Socket newSSLSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket(serverSocketAddress.getAddress(), serverSocketAddress.getPort());
    }

    
}
