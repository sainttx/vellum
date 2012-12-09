/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers
 */
package saltserver.app;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import saltserver.httphandler.*;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evans
 */
public class VaultHttpHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(VaultHttpHandler.class);
    VaultApp app;
    ValutStorage storage;
    
    public VaultHttpHandler(VaultApp app) {
        this.app = app;
        this.storage = app.getStorage();
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpHandler handler = getHandler(httpExchange);
        if (handler != null) {
            handler.handle(httpExchange);
        } else {
            
        }
    }
    
    public HttpHandler getHandler(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        logger.info("path", path);
        if (httpExchange.getRemoteAddress().getHostName().equals("127.0.0.1"))  {
            if (path.equals("/shutdown")) {
                return new VaultShutdownHandler(app);
            } else if (path.equals("/manager")) {
                return new VaultManagerHandler(app);
            } else if (path.startsWith("/local/")) {
                return null;
            }
        }
        if (path.startsWith("/getSecret/")) {
            return new GetSecretHandler(app);
        } else if (path.startsWith("/postSecret/")) {
            return new VaultSecretHandler(app);            
        } else if (path.equals("/admin")) {
            return new VaultAdminHandler(app);
        }
        return null;
    }
}
