
package dualcontrol;

import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 * @author evans
 */
public class DualControlKeyStores {    
    static final String keyStorePath = System.getProperty("dualcontrol.ssl.keyStore");
    static final char[] keyStorePassword = getPassword("dualcontrol.ssl.keyStorePassword");
    static final char[] keyPassword = getPassword("dualcontrol.ssl.keyPassword");
    static final String trustStorePath = System.getProperty("dualcontrol.ssl.trustStore");
    static final char[] trustStorePassword = getPassword("dualcontrol.ssl.trustStorePassword");    
    
    public static char[] getPassword(String propertyName) {
        return System.getProperty(propertyName).toCharArray();
    }
    
    public static SSLContext createSSLContext() throws Exception {
        return createSSLContext(keyStorePath, keyStorePassword, keyPassword,
                trustStorePath, trustStorePassword);
    }

    public static SSLContext createSSLContext(String keyStorePath, 
            char[] keyStorePassword, char[] keyPassword,
            String trustStorePath, char[] trustStorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(keyStorePath), keyStorePassword);
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream(trustStorePath), trustStorePassword);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, keyPassword);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(trustStore);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), 
                trustManagerFactory.getTrustManagers(), new SecureRandom());
        return sslContext;
    }
    
    public static KeyStore loadKeyStore(String keyStorePath, char[] keyStorePassword) 
            throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        if (keyStorePath.contains(":")) {
            String[] array = keyStorePath.split(":");
            Socket socket = DualControlKeyStores.createSSLContext().getSocketFactory().
                createSocket(array[0], Integer.parseInt(array[1]));
            keyStore.load(socket.getInputStream(), keyStorePassword);
            socket.close();
        } else if (new File(keyStorePath).exists()) {
            FileInputStream fis = new FileInputStream(keyStorePath);
            keyStore.load(fis, keyStorePassword);
            fis.close();
        } else {
            keyStore.load(null, keyStorePassword);            
        }
        return keyStore;
    }
}
