/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.insecure;

import crocserver.httphandler.access.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.httphandler.access.AccessHomePageHandler;
import crocserver.httphandler.secure.PostHandler;
import java.io.IOException;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import crocserver.storage.CrocStorage;

/**
 *
 * @author evans
 */
public class InsecureHttpHandler implements HttpHandler {

    Logr logger = LogrFactory.getLogger(InsecureHttpHandler.class);
    CrocStorage storage;

    public InsecureHttpHandler(CrocStorage storage) {
        this.storage = storage;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        new InsecureHomePageHandler(storage).handle(httpExchange);
    }
}
