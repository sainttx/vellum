
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
    static String keyStoreLocation = System.getProperty("dualcontrol.ssl.keyStore");
    static char[] keyStorePassword = getPassword("dualcontrol.ssl.keyStorePassword");
    static char[] keyPassword = getPassword("dualcontrol.ssl.keyPassword");
    static String trustStoreLocation = System.getProperty("dualcontrol.ssl.trustStore");
    static char[] trustStorePassword = getPassword("dualcontrol.ssl.trustStorePassword");    
    
    public static char[] getPassword(String propertyName) {
        return System.getProperty(propertyName).toCharArray();
    }
    
    public static SSLContext createSSLContext() throws Exception {
        return createSSLContext(keyStoreLocation, keyStorePassword, keyPassword,
                trustStoreLocation, trustStorePassword);
    }

    public static SSLContext createSSLContext(String keyStoreLocation, 
            char[] keyStorePassword, char[] keyPassword,
            String trustStoreLocation, char[] trustStorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(keyStoreLocation), keyStorePassword);
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream(trustStoreLocation), trustStorePassword);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, keyPassword);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(trustStore);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), 
                trustManagerFactory.getTrustManagers(), new SecureRandom());
        return sslContext;
    }
    
    public static KeyStore loadKeyStore(String keyStoreLocation, char[] keyStorePassword) 
            throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        if (keyStoreLocation.contains(":")) {
            String[] array = keyStoreLocation.split(":");
            Socket socket = DualControlKeyStores.createSSLContext().getSocketFactory().
                createSocket(array[0], Integer.parseInt(array[1]));
            keyStore.load(socket.getInputStream(), keyStorePassword);
            socket.close();
        } else if (new File(keyStoreLocation).exists()) {
            FileInputStream fis = new FileInputStream(keyStoreLocation);
            keyStore.load(fis, keyStorePassword);
            fis.close();
        } else {
            keyStore.load(null, keyStorePassword);            
        }
        return keyStore;
    }
}
