/*
 */
package venigmon.httpserver;

import bizstat.server.BizstatServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
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
public class BizstatHttpServer {
    Logr logger = LogrFactory.getLogger(BizstatHttpServer.class);
    BizstatServer context; 
    HttpServer httpServer;
    HttpServerConfig config;     
    ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 8, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(4));
    
    public BizstatHttpServer(BizstatServer context, HttpServerConfig config) {
        this.context = context;
        this.config  = config;
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
    
    public void start() throws Exception {
        waitPort(config.getPort(), 4000, 500);
        InetSocketAddress socketAddress = new InetSocketAddress(config.getPort());
        httpServer = HttpServer.create(socketAddress, 4);
        httpServer.setExecutor(executor);
        httpServer.createContext("/", createHomePageHandler());
        httpServer.createContext("/storage", createStoragePageHandler());
        httpServer.createContext("/post", createPostHandler());
        httpServer.start();
        logger.info("start", config.getPort());
    }

    public boolean stop() {
        if (httpServer != null) {
            httpServer.stop(0); 
            executor.shutdown();
            return true;
        }        
        return false;
    }

    private HttpHandler createHomePageHandler() {
        return new HttpHandler() {

            @Override
            public void handle(HttpExchange he) throws IOException {
                new HomePageHandler(context).handle(he);    
            }
        };        
    }
    
    private HttpHandler createStoragePageHandler() {
        return new HttpHandler() {

            @Override
            public void handle(HttpExchange he) throws IOException {
                new StoragePageHandler(context).handle(he);    
            }
        };        
    }

    private HttpHandler createPostHandler() {
        return new HttpHandler() {

            @Override
            public void handle(HttpExchange he) throws IOException {
                new PostHandler(context).handle(he);    
            }
        };        
    }
    
}
