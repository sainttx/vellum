/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package venigmon.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import venigmon.storage.VenigmonStorage;

/**
 *
 * @author evans
 */
public class VenigmonHttpHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(VenigmonHttpHandler.class);
    VenigmonStorage storage;

    public VenigmonHttpHandler(VenigmonStorage storage) {
        this.storage = storage;
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        logger.info("path", path);
        if (path.startsWith("/storage/")) {
            new StoragePageHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/view/serviceRecord/")) {
            new ViewServiceRecordPageHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/post/")) {
            new PostHandler(storage).handle(httpExchange);
        } else {
            new HomePageHandler(storage).handle(httpExchange);
        }        
    }
}
