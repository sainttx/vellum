/*
 */
package venigmon.httpserver;

import vellum.httpserver.VellumHttpServer;
import venigmon.storage.CrocStorage;

/**
 *
 * @author evans
 */
public class CrocHttpServer {
    VellumHttpServer httpServer;
    CrocStorage storage;
    
    public CrocHttpServer(CrocStorage storage, HttpServerConfig config) {
        this.storage = storage;
        httpServer = new VellumHttpServer(config);
    }    

    public void start() throws Exception {
        httpServer.start(new CrocHttpHandler(storage));
    }
    
    public boolean stop() {
        return httpServer.stop();        
    }
}
