/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012 Evan Summers, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2010 iPay (Pty) Ltd
 */
package crocserver.httphandler.admin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.httphandler.access.StoragePageHandler;
import java.io.IOException;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import crocserver.storage.common.CrocStorage;

/**
 *
 * @author evans
 */
public class AdminHttpHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(AdminHttpHandler.class);
    CrocStorage storage;
            
    public AdminHttpHandler(CrocStorage storage) {
        this.storage = storage;
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        logger.info("path", path);
        if (path.startsWith("/storage/")) {
            new StoragePageHandler(storage).handle(httpExchange);
        } else {
            new AdminHomePageHandler(storage).handle(httpExchange);
        }  
    }
}
