/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;

/**
 *
 * @author evan
 */
public class VCipherServer {
    Logr logger = LogrFactory.getLogger(getClass());    
    VCipherContext context;
    VCipherProperties properties;
    ServerSocket serverSocket; 
    boolean accepting = true;
    
    public VCipherServer() {
    }
    
    public void start(VCipherContext context) throws IOException {
        this.context = context;
        this.properties = context.properties;
        this.serverSocket = context.sslContext.getServerSocketFactory().createServerSocket(
                properties.sslPort, properties.backlog, context.inetAddress);
        while (accepting) {
            try {
                Socket socket = serverSocket.accept();
                VCipherThread thread = new VCipherThread(socket);
                thread.start();
            } catch (Exception e) {
                logger.warn(e);
            }
        }                    
    }        
    
    public void stop() throws IOException {        
        accepting = false;
        serverSocket.close();
    }
}
