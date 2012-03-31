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
    public static final VProviderContext instance = new VProviderContext();
    
    VProviderConfig properties;
    char[] keyStorePassword;
    char[] trustStorePassword;
    char[] keyPassword;
    InetAddress serverInetAddress;
    InetSocketAddress serverSocketAddress;
    
    KeyStore keyStore;
    KeyStore trustStore;
    SSLContext sslContext;
    KeyManager[] keyManagers;
    TrustManager[] trustManagers;
    SecureRandom secureRandom;

    private VProviderContext() {        
    }
    
    public void config(VProviderConfig properties, char[] keyStorePassword, char[] keyPassword, char[] trustStorePassword) throws Exception {
        this.properties = properties;
        this.keyStorePassword = keyStorePassword;        
        this.trustStorePassword = trustStorePassword;        
        this.keyPassword = keyPassword;        
        this.serverInetAddress = InetAddress.getByName(properties.serverIp);
        this.serverSocketAddress = new InetSocketAddress(serverInetAddress, properties.sslPort);
        initKeyManagers();
        initTrustManagers();
        initSSLContext();
    }
    
    public void init() throws IOException {
        VProviderConnection connection = new VProviderConnection();
        connection.open();
        connection.close();      
    }
    
    private void initKeyManagers() throws Exception {
        keyStore = KeyStore.getInstance("JCEKS");
        InputStream inputStream = new FileInputStream(properties.keyStore);
        keyStore.load(inputStream, keyStorePassword);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, keyPassword);
        this.keyManagers = keyManagerFactory.getKeyManagers();
    }

    private void initTrustManagers() throws Exception {
        trustStore = KeyStore.getInstance("JCEKS");
        InputStream inputStream = new FileInputStream(properties.trustStore);
        trustStore.load(inputStream, trustStorePassword);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(trustStore);        
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

    public Socket createSocket() throws IOException {
        if (false) {
            return new Socket(serverSocketAddress.getAddress(), serverSocketAddress.getPort());
        } else {
            return sslContext.getSocketFactory().createSocket(serverSocketAddress.getAddress(), serverSocketAddress.getPort());
        }
    }

    public static VProviderContext getInstance() {
        return instance;
    }
        
    
}
