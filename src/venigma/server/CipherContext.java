/*
 * Copyright Evan Summers
 * 
 */
package venigma.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.Key;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.TrustManagerFactory;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;

/**
 *
 * @author evan
 */
public class CipherContext {
    Logr logger = LogrFactory.getLogger(getClass());
    CipherConfig config;    
    CipherProperties properties;  
    CipherRequestAuth requestAuth;
    SecureRandom sr = new SecureRandom();
    SSLContext sslContext;
    InetSocketAddress address;
    InetAddress inetAddress;
    CipherStorage storage = new CipherStorage();
    boolean started = false;
    Key key; 
    SSLServerSocket serverSocket; 
    
    public CipherContext() {
    }

    public void config(CipherConfig config, CipherProperties properties) throws Exception {
        this.config = config;        
        this.properties = properties;
        inetAddress = InetAddress.getByName(config.serverIp);
        address = new InetSocketAddress(inetAddress, config.sslPort);
        sslContext = SSLContext.getInstance("TLS");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        KeyStore ks = KeyStore.getInstance("JCEKS");
        File keyStoreFile = new File(config.privateKeyStore);
        logger.info(keyStoreFile.getAbsolutePath());
        FileInputStream fis = new FileInputStream(keyStoreFile);
        ks.load(fis, properties.keyStorePassword);
        kmf.init(ks, properties.privateKeyPassword);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        KeyStore ts = KeyStore.getInstance("JCEKS");
        FileInputStream trustStoreStream = new FileInputStream(config.trustKeyStore);
        ts.load(trustStoreStream, properties.trustKeyStorePassword);
        tmf.init(ts);
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), sr);
        logger.info("cipher ssl context initialised", hashCode());
        loadKey();
        initServerSocket();
        storage.init(properties.userList); 
        requestAuth = new CipherRequestAuth(this);
    }

    private void loadKey() throws Exception {
        key = loadKey(config.secretKeyStore, config.secretAlias, properties.secretKeyStorePassword, properties.secretKeyPassword);                        
    }
        
    private void initServerSocket() throws IOException {
        this.serverSocket = (SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(
                config.sslPort, config.backlog, inetAddress);
        this.serverSocket.setNeedClientAuth(true);
    }

    public SSLServerSocket getServerSocket() {
        return serverSocket;
    }
    
    private Key loadKey(String keyStoreFile, String keyAlias, char[] storePass, char[] keyPass) throws Exception {
        File file = new File(keyStoreFile);
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(new FileInputStream(file), storePass);
        logger.info("loadKey", keyStore.getType(), keyStore.getProvider().getName());
        Key key = keyStore.getKey(keyAlias, keyPass);
        logger.info(key.getAlgorithm(), key.getFormat());
        return key;
    }
    
    public Cipher getCipher(int opmode, byte[] iv) throws Exception {
        IvParameterSpec ips = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(opmode, key, ips);
        return cipher;
    }

    public Cipher getCipher(int opmode) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(opmode, key);
        return cipher;
    }
    
    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
        if (true) {
        }
    }

        
}
