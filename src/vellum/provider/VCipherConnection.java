/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author evan
 */
public class VCipherConnection {
    final InetSocketAddress address;
    VCipherSocket socket;
    SSLContext sslContext;
    
    public VCipherConnection(InetSocketAddress address) {
        this.address = address;
    }

    private void open() throws IOException {
        SSLSocket sslSocket = null;
        socket = new VCipherSocket(sslSocket);
    }
    
     
    public VCipherResponse sendRequest(VCipherRequest request) throws IOException {
        while (true) {
            try {
                return socket.sendRequest(request);
            } catch (IOException e) {
                open();
            } finally {
            }
        }
    }
    
}
