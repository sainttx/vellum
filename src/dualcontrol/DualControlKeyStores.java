
package dualcontrol;

import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 * @author evans
 */
public class DualControlKeyStores {    

    public static char[] getPassword(String propertyName, char[] defaultValue) {
        String passwordString = System.getProperty(propertyName);
        if (passwordString == null) {
            return defaultValue;
        }
        return passwordString.toCharArray();
    }

    public static SSLContext createSSLContext(String keyStoreLocation, char[] keyStorePassword)
            throws Exception {    
        return DualControlKeyStores.createSSLContext(keyStoreLocation, keyStorePassword,
                keyStorePassword, keyStoreLocation, keyStorePassword);
    }
        
    public static SSLContext createSSLContext() throws Exception {
        char[] keyStorePassword = null;
        char[] keyPassword = null;
        char[] trustStorePassword = null;
        try {
            String keyStoreLocation = System.getProperty("dualcontrol.ssl.keyStore");
            if (keyStoreLocation == null) {
                throw new Exception("Missing property -Ddualcontrol.ssl.keyStore");
            }
            keyStorePassword = getPassword("dualcontrol.ssl.keyStorePassword", null);
            if (keyStorePassword == null) {
                keyStorePassword = System.console().readPassword(
                        "Enter passphrase for dual control SSL connection: ");
            }
            keyPassword = getPassword("dualcontrol.ssl.keyPassword", keyStorePassword);
            String trustStoreLocation =
                    System.getProperty("dualcontrol.ssl.trustStore", keyStoreLocation);
            trustStorePassword =
                    getPassword("dualcontrol.ssl.trustStorePassword", keyStorePassword);
            return createSSLContext(keyStoreLocation, keyStorePassword, keyPassword,
                    trustStoreLocation, trustStorePassword);
        } finally {
            if (keyStorePassword != null) {
                Arrays.fill(keyStorePassword, (char) 0);
            }
            if (keyPassword != null) {
                Arrays.fill(keyPassword, (char) 0);
            }
            if (trustStorePassword != null) {
                Arrays.fill(trustStorePassword, (char) 0);
            }
        }
    }
    
    public static void clearPassword(char[] password) {
        if (password != null) {
            Arrays.fill(password, (char) 0);
        }
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
