/*
 */
package venigmon.httpserver;

import venigmon.storage.CrocStorage;

/**
 *
 * @author evans
 */
public class VenigmonHttpServer {
    VellumHttpServer httpServer;
    CrocStorage storage;
    
    public VenigmonHttpServer(CrocStorage storage, HttpServerConfig config) {
        this.storage = storage;
        httpServer = new VellumHttpServer(config);
    }    

    public void start() throws Exception {
        httpServer.start(new VenigmonHttpHandler(storage));
    }
    
    public boolean stop() {
        return httpServer.stop();        
    }
}
