/*
 */
package crocserver.app;

import crocserver.httphandler.secure.SecureHttpHandler;
import crocserver.httpserver.HttpServerConfig;
import vellum.httpserver.VellumHttpServer;

/**
 *
 * @author evans
 */
public class CrocHttpServer {
    VellumHttpServer httpServer;
    CrocApp app;
    
    public CrocHttpServer(CrocApp app, HttpServerConfig config) {
        this.app = app;;
        httpServer = new VellumHttpServer(config);
    }    
    
    public void start() throws Exception {
        httpServer.start(new SecureHttpHandler(app));
    }
    
    public boolean stop() {
        return httpServer.stop();
    }
}
