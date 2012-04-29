/*
 * Copyright Evan Summers
 * 
 */
package venigma.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.TrustManagerFactory;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;
import vellum.util.Streams;
import venigma.common.AdminUser;
import venigma.server.storage.CipherStorage;
import venigma.common.KeyInfo;
import venigma.server.storage.StorageExceptionType;
import venigma.server.storage.StorageRuntimeException;

/**
 *
 * @author evan
 */
public class CipherContext {

    Logr logger = LogrFactory.getLogger(getClass());
    CipherRequestAuth requestAuth = new CipherRequestAuth(this);
    CipherConfig config;
    CipherProperties properties;
    SecureRandom sr = new SecureRandom();
    SSLContext sslContext;
    InetSocketAddress address;
    InetAddress inetAddress;
    CipherStorage storage = new CipherStorage();
    boolean started = false;
    boolean loaded = false;
    SSLServerSocket serverSocket;
    KeyStore secretKeyStore;
    Map<String, SecretKey> keyMap = new HashMap();
    
    public CipherContext() {
    }

    public void config(CipherConfig config, CipherProperties properties) throws Exception {
        this.config = config;
        this.properties = properties;
        storage.init(properties.dataStorePassword);
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
        initServerSocket();
    }

    private void initServerSocket() throws IOException {
        this.serverSocket = (SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(
                config.sslPort, config.backlog, inetAddress);
        this.serverSocket.setNeedClientAuth(true);
    }

    public SSLServerSocket getServerSocket() {
        return serverSocket;
    }

    public SecretKey getSecretKey(KeyInfo keyInfo) throws Exception {
        String alias = keyInfo.buildKeystoreAlias();
        SecretKey secretKey = keyMap.get(alias);
        if (secretKey == null) {
            secretKey = loadSecretKey(keyInfo.buildKeystoreAlias(), properties.secretKeyPassword);        
            keyMap.put(alias, secretKey);
        }
        return secretKey;
    }

    private synchronized void loadKeyStore() throws Exception {
        if (!loaded) {
            secretKeyStore = KeyStore.getInstance("JCEKS");
            File file = new File(config.secretKeyStore);
            if (file.exists()) {
                FileInputStream stream = new FileInputStream(file);
                secretKeyStore.load(stream, properties.secretKeyStorePassword);
                stream.close();
                loaded = true;
            } else {
                secretKeyStore.load(null, null);
            }
        }
    }

    private synchronized void saveKeyStore() throws Exception {
        String newFileName = config.secretKeyStore + ".new";
        FileOutputStream stream = new FileOutputStream(newFileName);
        secretKeyStore.store(stream, properties.secretKeyStorePassword);
        stream.close();
        Streams.renameTo(newFileName, config.secretKeyStore);
        loaded = false;
    }
    
    private SecretKey loadSecretKey(String keyAlias, char[] keyPass) throws Exception {
        logger.info("loadKey", keyAlias, secretKeyStore.getType(), secretKeyStore.getProvider().getName());
        loadKeyStore();
        SecretKey secretKey = (SecretKey) secretKeyStore.getKey(keyAlias, keyPass);
        if (secretKey == null) {
            throw new StorageRuntimeException(StorageExceptionType.KEY_NOT_FOUND, keyAlias);
        }
        logger.info("loadKey", secretKey.getAlgorithm(), secretKey.getFormat());
        return secretKey;
    }
    
    public void saveNewKey(KeyInfo keyInfo) throws Exception {
        logger.info("saveNewKey", keyInfo);        
        loadKeyStore();
        String keyAlias = keyInfo.buildKeystoreAlias();
        logger.info("saveNewKey keyAlias", keyAlias);        
        if (secretKeyStore.isKeyEntry(keyAlias)) {
            throw new StorageRuntimeException(StorageExceptionType.KEY_ALREADY_EXISTS, keyAlias);
        }
        KeyGenerator aes = KeyGenerator.getInstance("AES");
        aes.init(keyInfo.getKeySize(), sr);
        SecretKey key = aes.generateKey();
        KeyStore.Entry entry = new KeyStore.SecretKeyEntry(key);
        KeyStore.ProtectionParameter prot = new KeyStore.PasswordProtection(properties.secretKeyPassword);
        secretKeyStore.setEntry(keyAlias, entry, prot);        
        storage.getKeyInfoStorage().add(keyInfo);
        saveKeyStore();
    }

    public void saveRevisedKey(KeyInfo keyInfo) throws Exception {
        logger.info("saveRevisedKey", keyInfo);
        loadKeyStore();
        String keyAlias = keyInfo.buildKeystoreAlias();
        logger.info("saveRevised keyAlias", keyAlias);        
        keyInfo.incrementRevisionNumber();
        KeyGenerator aes = KeyGenerator.getInstance("AES");
        aes.init(keyInfo.getKeySize(), sr);
        SecretKey key = aes.generateKey();
        KeyStore.Entry entry = new KeyStore.SecretKeyEntry(key);
        KeyStore.ProtectionParameter prot = new KeyStore.PasswordProtection(properties.secretKeyPassword);
        secretKeyStore.setEntry(keyInfo.buildKeystoreAlias(), entry, prot);        
        storage.getKeyInfoStorage().update(keyInfo);
        saveKeyStore();
    }

    public Cipher getCipher(KeyInfo keyInfo, int opmode, byte[] iv) throws Exception {
        IvParameterSpec ips = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKey secretKey = getSecretKey(keyInfo);
        cipher.init(opmode, secretKey, ips);
        return cipher;
    }

    public Cipher getCipher(KeyInfo keyInfo, int opmode) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKey secretKey = getSecretKey(keyInfo);
        cipher.init(opmode, secretKey);
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

    public CipherStorage getStorage() {
        return storage;
    }

    public void init(List<AdminUser> userLit) {
        storage.getAdminUserStorage().init(userLit);
    }

}
