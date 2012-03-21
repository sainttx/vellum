/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author evan
 */
public class VCipherConnection {
    VCipherSocket socket;
    VProvider provider = VProvider.instance;
    
    public VCipherConnection() {
    }

    private void open() throws IOException {
        Socket sslSocket = provider.newSSLSocket();
        this.socket = new VCipherSocket(sslSocket);
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
