/*
 */
package venigmon.httpserver;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evans
 */
public class VellumHttpServer {
    private Logr logger = LogrFactory.getLogger(VellumHttpServer.class);
    HttpServer httpServer;
    HttpServerConfig config;     
    ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 8, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(4));
    
    public VellumHttpServer(HttpServerConfig config) {
        this.config = config;
    }    

    private boolean portAvailable(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.close();
            return true;
        } catch (Exception e) {
            logger.warn("portAvailable", e.getMessage());
            return false;
        }
    }
    
    private boolean waitPort(int port, long millis, long sleep) {
        long time = System.currentTimeMillis() + millis;
        while (!portAvailable(port)) {
            if (System.currentTimeMillis() > time) {
                return false;
            }
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                logger.warn(e, "waitPort");
            }
        }
        return true;
    }

    public void start(HttpHandler httpHandler) throws Exception {
        start();
        httpServer.createContext("/", httpHandler);
    }
    
    public void start() throws Exception {
        waitPort(config.getPort(), 4000, 500);
        InetSocketAddress socketAddress = new InetSocketAddress(config.getPort());
        httpServer = HttpServer.create(socketAddress, 4);
        httpServer.setExecutor(executor);
        httpServer.start();
        logger.info("start", config.getPort());
    }

    public void createContext(String contextName, HttpHandler httpHandler) {
        httpServer.createContext(contextName, httpHandler);
    }

    public boolean stop() {
        if (httpServer != null) {
            httpServer.stop(0); 
            executor.shutdown();
            return true;
        }        
        return false;
    }    
}
