/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;
import vellum.util.Streams;

/**
 *
 * @author evan
 */
public class VCipherServer extends Thread implements Closeable {
    Logr logger = LogrFactory.getLogger(this);
    VCipherContext context;
    ServerSocket serverSocket; 
    boolean accepting = true;
    
    public VCipherServer() {
    }
    
    public void config(VCipherContext context) throws Exception {
        this.context = context;
        if (false) {
            this.serverSocket = new ServerSocket(
                    context.config.sslPort, context.config.backlog, context.inetAddress);
        } else {
            this.serverSocket = context.sslContext.getServerSocketFactory().createServerSocket(
                    context.config.sslPort, context.config.backlog, context.inetAddress);
        }
    }
    
    @Override
    public void run() {
        while (accepting) {
            try {
                Socket socket = serverSocket.accept();
                logger.info("socket accepted", socket.getRemoteSocketAddress());
                VCipherHandler handler = new VCipherHandler(context);
                handler.init();
                handler.handle(socket);
            } catch (SocketException e) {
                logger.warn(e.getMessage());
            } catch (Exception e) {
                logger.warn(e);
            }
        }                    
        Streams.close(serverSocket);
    }        
    
    @Override
    public void close() throws IOException {        
        accepting = false;
        Streams.close(serverSocket);
    }
}
