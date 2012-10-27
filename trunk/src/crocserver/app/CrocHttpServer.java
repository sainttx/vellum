/*
 */
package crocserver.app;

import crocserver.httpserver.SecureHttpHandler;
import crocserver.httpserver.HttpServerConfig;
import crocserver.storage.CrocStorage;
import vellum.httpserver.VellumHttpServer;

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
        httpServer.start(new SecureHttpHandler(storage));
    }
    
    public boolean stop() {
        return httpServer.stop();
    }
}
