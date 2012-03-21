/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import java.net.InetAddress;
import java.security.Provider;
import java.security.Security;

/**
 *
 * @author evan
 */

public class VProvider extends Provider {
    public static final String CHARSET = "UTF8";
    public static VProvider instance = new VProvider();
    
    char[] password;
    InetAddress serverAddress;
    int serverPort;
    
    
    
    public void config(String password) {
        this.password = password.toCharArray();        
        Security.addProvider(VProvider.instance);
    }
        
    VProvider() {
        super("VProvider", 1.0, "Provides KeyStore.JKS");
        put("KeyStore.JKS", VKeyStoreSpi.class.getName());
    }

    public char[] getPassword() {
        return password;
    }

    public VCipherConnection newConnection() {
        return new VCipherConnection(serverAddress, serverPort);
    }
        
}
