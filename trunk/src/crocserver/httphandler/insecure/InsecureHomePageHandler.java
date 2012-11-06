/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.insecure;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.CrocApp;
import crocserver.httpserver.HttpExchangeInfo;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Streams;

/**
 *
 * @author evans
 */
public class InsecureHomePageHandler implements HttpHandler {

    Logr logger = LogrFactory.getLogger(InsecureHomePageHandler.class);
    CrocApp app;

    public InsecureHomePageHandler(CrocApp app) {
        super();
        this.app = app;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        HttpExchangeInfo httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        String path = httpExchange.getRequestURI().getPath();
        if (httpExchange.getRequestURI().getPath().endsWith(".png")) {
            httpExchange.getResponseHeaders().set("Content-type", "image/x-png");
        } else if (httpExchange.getRequestURI().getPath().endsWith(".html")) {
            httpExchange.getResponseHeaders().set("Content-type", "text/html");
        } else if (httpExchange.getRequestURI().getPath().endsWith(".css")) {
            httpExchange.getResponseHeaders().set("Content-type", "text/css");
        } else if (httpExchange.getRequestURI().getPath().endsWith(".js")) {
            httpExchange.getResponseHeaders().set("Content-type", "text/javascript");
        } else if (httpExchange.getRequestURI().getPath().endsWith(".txt")) {
            httpExchange.getResponseHeaders().set("Content-type", "text/plain");
        } else if (httpExchange.getRequestURI().getPath().endsWith(".html")) {
            httpExchange.getResponseHeaders().set("Content-type", "text/html");
        } else {
            httpExchange.getResponseHeaders().set("Content-type", "text/html");
            path = "/index.html";
        }
        String resourceName = "/crocserver/web" + path;
        logger.info("resource", resourceName);
        try {
            InputStream resourceStream = getClass().getResourceAsStream(resourceName);
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            Streams.transmit(resourceStream, httpExchange.getResponseBody());
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            logger.warn(e);
        }
        httpExchange.close();
    }
}
