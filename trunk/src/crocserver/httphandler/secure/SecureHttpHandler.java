/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.secure;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.CrocApp;
import java.io.IOException;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evans
 */
public class SecureHttpHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(SecureHttpHandler.class);
    CrocApp app;
            
    public SecureHttpHandler(CrocApp app) {
        this.app = app;
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        logger.info("path", path);
        if (path.startsWith("/post/")) {
            new PostHandler(app).handle(httpExchange);
        } else {
            new SecureHomeHandler1(app).handle(httpExchange);
        }  
    }
}
