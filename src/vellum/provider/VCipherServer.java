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
public class VCipherServer extends Thread {
    Logr logger = LogrFactory.getLogger(getName());    
    VCipherContext context;
    VCipherProperties properties;
    ServerSocket serverSocket; 
    boolean accepting = true;
    
    public VCipherServer() {
    }
    
    public void config(VCipherContext context) throws IOException {
        this.context = context;
        this.properties = context.properties;
        this.serverSocket = context.sslContext.getServerSocketFactory().createServerSocket(
                properties.sslPort, properties.backlog, context.inetAddress);
    }
    
    @Override
    public void run() {
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
    
    public void close() throws IOException {        
        accepting = false;
        serverSocket.close();
        super.interrupt();
    }
}















