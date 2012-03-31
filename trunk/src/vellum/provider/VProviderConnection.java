/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import java.io.IOException;
import java.net.Socket;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;
import vellum.util.Streams;

/**
 *
 * @author evan
 */
public class VProviderConnection {
    Logr logger = LogrFactory.getLogger(getClass());
    Socket socket;
    VProviderContext providerContext = VProviderContext.instance;
    
    public VProviderConnection() {
    }

    public void open() throws IOException {
        if (socket != null) {
            logger.warn("already open");
            Streams.close(socket);
        }
        this.socket = providerContext.createSocket();
        logger.info("open", socket.getRemoteSocketAddress());
        if (false) {
            VCipherResponse response = sendCipherRequest(new VCipherRequest(VCipherRequestType.PING));
            logger.info("open ping response", response);
        }
    }

    public void close() throws IOException {
        if (socket == null) {
            logger.warn("already closed");
        } else {
            logger.info("close", socket.getRemoteSocketAddress());            
            socket.close();
            socket = null;
        } 
    }
    
    public VCipherResponse sendCipherRequest(VCipherRequest request) throws IOException {
        logger.info("send", request);
        return (VCipherResponse) sendSingleRequest(request, VCipherResponse.class);
    }    
    
    public Object sendSingleRequest(Object request, Class responseClass) throws IOException {
        try {
            if (socket == null) open();
            VSockets.write(socket, request);
            Object response = VSockets.read(socket, responseClass);
            return response;
        } finally {
            close();
        }
    }    
}
