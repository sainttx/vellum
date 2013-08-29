/*
 * Apache Software License 2.0, (c) Copyright 2013, Evan Summers
 * 
 */

package dualcontrol;

import vellum.util.VellumProperties;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.security.KeyStore;
import org.apache.log4j.Logger;

/**
 *
 * @author evans
 */
public class DualControlKeyStores {    
    static Logger logger = Logger.getLogger(DualControlKeyStores.class);
    
    public static KeyStore loadKeyStore(String keyStoreLocation, char[] keyStorePassword) 
            throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        if (keyStoreLocation.contains(":")) {
            String[] array = keyStoreLocation.split(":");
            Socket socket = DualControlSSLContextFactory.createSSLContext(
                    VellumProperties.systemProperties).getSocketFactory().
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
    
    public static KeyStore loadLocalKeyStore(String keyStoreLocation, String keyStoreType, 
            char[] keyStorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        if (new File(keyStoreLocation).exists()) {
            FileInputStream fis = new FileInputStream(keyStoreLocation);
            keyStore.load(fis, keyStorePassword);
            fis.close();
        } else {
            keyStore.load(null, keyStorePassword);            
        }
        return keyStore;
    }
    
    
}
