/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers
 */
package saltserver.app;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import saltserver.httphandler.GetSecretHandler;
import saltserver.httphandler.PostSecretHandler;
import saltserver.httphandler.SaltManagerHandler;
import saltserver.httphandler.ShutdownHandler;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evans
 */
public class SaltHttpHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(SaltHttpHandler.class);
    SaltApp app;
    SaltStorage storage;
    
    public SaltHttpHandler(SaltApp app) {
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
                return new ShutdownHandler(app);
            } else if (path.equals("/manager")) {
                return new SaltManagerHandler(app);
            } else if (path.startsWith("/local/")) {
                return null;
            }
        }
        if (path.startsWith("/getSecret/")) {
            return new GetSecretHandler(app);
        } else if (path.startsWith("/postSecret/")) {
            return new PostSecretHandler(app);            
        }
        return null;
    }
}
