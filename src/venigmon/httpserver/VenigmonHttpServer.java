/*
 */
package venigmon.httpserver;

import venigmon.storage.VenigmonStorage;

/**
 *
 * @author evans
 */
public class VenigmonHttpServer {
    VellumHttpServer httpServer;
    VenigmonStorage storage;
    
    public VenigmonHttpServer(VenigmonStorage storage, HttpServerConfig config) {
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
