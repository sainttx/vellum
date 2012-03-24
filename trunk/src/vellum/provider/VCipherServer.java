/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;

/**
 *
 * @author evan
 */
public class VCipherServer extends Thread implements Closeable {
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
        if (false) {
            this.serverSocket = new ServerSocket(
                    properties.sslPort, properties.backlog, context.inetAddress);
        } else {
            this.serverSocket = context.sslContext.getServerSocketFactory().createServerSocket(
                properties.sslPort, properties.backlog, context.inetAddress);
        }
    }
    
    @Override
    public void run() {
        while (accepting) {
            try {
                Socket socket = serverSocket.accept();
                logger.trace("socket accepted", socket.getRemoteSocketAddress());
                VCipherThread thread = new VCipherThread(socket);
                thread.start();
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }                    
    }        
    
    @Override
    public void close() throws IOException {        
        accepting = false;
        serverSocket.close();
        super.interrupt();
    }
}















