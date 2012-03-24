/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import vellum.enigma.*;
import java.net.Socket;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;

/**
 *
 * @author evan
 */
public class VCipherThread extends Thread {
    Logr logger = LogrFactory.getLogger(getName());    
    VSocket socket;
    
    public VCipherThread(Socket clientSocket) {
        this.socket = new VSocket(clientSocket);
    }
    
    @Override
    public void run() {
        try {
            process();
        } catch (Exception e) {
            logger.warn(e);
        } finally {
        }
    }
    
    protected void process() throws Exception {
        VCipherRequest request = socket.read(VCipherRequest.class);
        logger.info("received", request);
        VCipherResponse response = new VCipherResponse(VCipherResponseType.OK, request.getBytes());
        logger.info("reply", response);
        socket.write(response);
    }
}
