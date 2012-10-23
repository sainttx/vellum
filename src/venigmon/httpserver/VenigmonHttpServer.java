/*
 */
package venigmon.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import venigmon.storage.VenigmonStorage;

/**
 *
 * @author evans
 */
public class VenigmonHttpServer extends VellumHttpServer {
    Logr logger = LogrFactory.getLogger(VenigmonHttpServer.class);
    VenigmonStorage storage;
    
    public VenigmonHttpServer(VenigmonStorage storage, HttpServerConfig config) {
        super(config);
        this.storage = storage;
    }    

    @Override
    public void start() throws Exception {
        super.start();
        httpServer.createContext("/", createStoragePageHandler());
        httpServer.createContext("/home", createHomePageHandler());
        httpServer.createContext("/storage", createStoragePageHandler());
        httpServer.createContext("/post", createPostHandler());        
    }
    
    private HttpHandler createHomePageHandler() {
        return new HttpHandler() {

            @Override
            public void handle(HttpExchange he) throws IOException {
                new HomePageHandler(storage).handle(he);    
            }
        };        
    }

    private HttpHandler createStoragePageHandler() {
        return new HttpHandler() {

            @Override
            public void handle(HttpExchange he) throws IOException {
                new StoragePageHandler(storage).handle(he);    
            }
        };        
    }
    
    private HttpHandler createPostHandler() {
        return new HttpHandler() {

            @Override
            public void handle(HttpExchange he) throws IOException {
                new PostHandler(storage).handle(he);    
            }
        };        
    }
}
