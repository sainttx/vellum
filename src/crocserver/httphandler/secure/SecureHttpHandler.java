/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.secure;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import crocserver.storage.CrocStorage;

/**
 *
 * @author evans
 */
public class SecureHttpHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(SecureHttpHandler.class);
    CrocStorage storage;
            
    public SecureHttpHandler(CrocStorage storage) {
        this.storage = storage;
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        logger.info("path", path);
        if (path.startsWith("/post/")) {
            new PostHandler(storage).handle(httpExchange);
        } else {
            new SecureHomePageHandler(storage).handle(httpExchange);
        }        
    }
}
