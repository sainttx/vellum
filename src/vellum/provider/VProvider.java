/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Provider;
import java.security.Security;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author evan
 */

public class VProvider extends Provider {
    public static final String CHARSET = "UTF8";
    public static VProvider instance = new VProvider();

    VProperties properties;
    VContext context;
    SSLContext sslContext;
    char[] password;
    InetSocketAddress serverSocketAddress;
            
    public void config(String password) throws UnknownHostException {
        this.password = password.toCharArray();        
        Security.addProvider(VProvider.instance);
        properties = new VProperties();
        context = new VContext(properties);
        sslContext = context.getSSLContext();
        InetAddress serverInetAddress = InetAddress.getByName(properties.serverIp);
        this.serverSocketAddress = new InetSocketAddress(serverInetAddress, properties.sslPort);
    }
        
    VProvider() {
        super("VProvider", 1.0, "Provides KeyStore.JKS");
        put("KeyStore.JKS", VKeyStoreSpi.class.getName());
    }

    public char[] getPassword() {
        return password;
    }

    public VCipherConnection newConnection() {
        return new VCipherConnection();
    }

    public Socket newSSLSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket(serverSocketAddress.getAddress(), serverSocketAddress.getPort());
    }

    
}
