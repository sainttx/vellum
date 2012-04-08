/*
 * Copyright Evan Summers
 * 
 */
package venigma.server;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.List;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;
import venigma.common.AdminUser;

/**
 *
 * @author evan
 */
public class CipherContext {
    Logr logger = LogrFactory.getLogger(getClass());
    CipherConfig config;    
    CipherProperties properties;    
    SecureRandom sr = new SecureRandom();
    SSLContext sslContext;
    InetSocketAddress address;
    InetAddress inetAddress;
    CipherStorage storage = new CipherStorage();
    
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
        File keyStoreFile = new File(config.keyStore);
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
    }
    
    public void load(List<AdminUser> userList) throws Exception {
        storage.init(userList);
    }
    
}
