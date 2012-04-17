/*
 * Copyright Evan Summers
 * 
 */
package venigma.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import javax.net.ssl.SSLSocket;
import vellum.logger.Logr;
import vellum.logger.LogrFactory;
import vellum.util.Streams;

/**
 *
 * @author evan
 */
public class CipherServer extends Thread implements Closeable {
    Logr logger = LogrFactory.getLogger(this);
    CipherContext context;
    ServerSocket serverSocket; 
    boolean accepting = true;
    
    public CipherServer() {
    }
    
    public void config(CipherContext context) throws Exception {
        this.context = context;
        this.serverSocket = context.getServerSocket();
    }
    
    @Override
    public void run() {
        while (accepting) {
            try {
                SSLSocket socket = (SSLSocket) serverSocket.accept();
                logger.info("socket accepted", socket.getClass(), socket.getRemoteSocketAddress());
                CipherHandler handler = new CipherHandler(context);
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
