/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012 Evan Summers, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2010 iPay (Pty) Ltd
 */
package crocserver.httphandler.insecure;

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
public class InsecureHttpHandler implements HttpHandler {

    Logr logger = LogrFactory.getLogger(InsecureHttpHandler.class);
    CrocApp app;

    public InsecureHttpHandler(CrocApp app) {
        this.app = app;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        new InsecureHomePageHandler(app).handle(httpExchange);
    }
}
