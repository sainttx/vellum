/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import javax.net.ssl.SSLContext;

/**
 *
 * @author evan
 */
public class VCipherServer {
    InetAddress address;
    int port;
    int backlog;
    ServerSocket serverSocket;

    public VCipherServer(SSLContext sslContext, int port, int backlog, InetAddress address) {
        this.address = address;
        this.port = port;
    }
    
    public void init(SSLContext sslContext) throws IOException {
        this.serverSocket = sslContext.getServerSocketFactory().createServerSocket(port, backlog, address);        
    }
    
}
