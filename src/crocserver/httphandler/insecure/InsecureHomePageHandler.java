/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */
package crocserver.httphandler.insecure;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.CrocApp;
import crocserver.httphandler.access.WebHandler;
import java.io.IOException;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evans
 */
public class InsecureHomePageHandler implements HttpHandler {

    Logr logger = LogrFactory.getLogger(InsecureHomePageHandler.class);
    CrocApp app;
    WebHandler webHandler;
    
    public InsecureHomePageHandler(CrocApp app) {
        super();
        this.app = app;
        webHandler = new WebHandler(app);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        webHandler.handle(httpExchange);
    }
}
