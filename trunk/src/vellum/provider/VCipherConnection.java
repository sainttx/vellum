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
    VSocket socket;
    VProviderContext providerContext = VProviderContext.instance;
    
    public VCipherConnection() {
    }

    private void open() throws IOException {
        Socket sslSocket = providerContext.newSSLSocket();
        this.socket = new VSocket(sslSocket);
    }
    
     
    public VCipherResponse sendRequest(VCipherRequest request) throws IOException {
        while (true) {
            try {
                return  sendCipherRequest(request);
            } catch (IOException e) {
                open();
            } finally {
            }
        }
    }

    public VCipherResponse sendCipherRequest(VCipherRequest request) throws IOException {
        return (VCipherResponse) sendSingleRequest(request, VCipherResponse.class);
    }    
    
    public Object sendSingleRequest(Object request, Class responseClass) throws IOException {
        socket.write(request);
        Object response = socket.read(responseClass);
        return response;
    }    
}
