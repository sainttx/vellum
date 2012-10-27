/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httpserver;

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
public class PublicCrocHttpHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(PublicCrocHttpHandler.class);
    CrocStorage storage;
            
    public PublicCrocHttpHandler(CrocStorage storage) {
        this.storage = storage;
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        logger.info("path", path);
        new HomePageHandler(storage).handle(httpExchange);
    }
}
