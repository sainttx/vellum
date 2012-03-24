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
public class VProviderConnection {
    Logr logger = LogrFactory.getLogger(getClass());
    VSocket socket;
    VProviderContext providerContext = VProviderContext.instance;
    
    public VProviderConnection() {
    }

    private void open() throws IOException {
        Socket sslSocket = providerContext.createSocket();        
        this.socket = new VSocket(sslSocket);
        logger.info("opened", sslSocket.getRemoteSocketAddress());
    }
         
    public VCipherResponse sendCipherRequest(VCipherRequest request) throws IOException {
        logger.info("send", request);
        return (VCipherResponse) sendSingleRequest(request, VCipherResponse.class);
    }    
    
    public Object sendSingleRequest(Object request, Class responseClass) throws IOException {
        if (socket == null) open();
        socket.write(request);
        Object response = socket.read(responseClass);
        return response;
    }    
}
