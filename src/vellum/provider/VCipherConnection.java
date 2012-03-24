/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import java.io.IOException;
import java.net.Socket;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;

/**
 *
 * @author evan
 */
public class VCipherConnection {
    Logr logger = LogrFactory.getLogger(getClass());
    VSocket socket;
    VProviderContext providerContext = VProviderContext.instance;
    
    public VCipherConnection() {
    }

    private void open() throws IOException {
        Socket sslSocket = providerContext.newSSLSocket();
        this.socket = new VSocket(sslSocket);
    }
         
    public VCipherResponse sendRequest(VCipherRequest request) throws IOException {
        return  sendCipherRequest(request);
    }

    public VCipherResponse sendCipherRequest(VCipherRequest request) throws IOException {
        return (VCipherResponse) sendSingleRequest(request, VCipherResponse.class);
    }    
    
    public Object sendSingleRequest(Object request, Class responseClass) throws IOException {
        if (socket == null) open();
        socket.write(request);
        Object response = socket.read(responseClass);
        return response;
    }    
}
