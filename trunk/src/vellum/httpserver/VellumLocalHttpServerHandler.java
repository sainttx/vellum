/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */
package vellum.httpserver;

import crocserver.httphandler.access.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Streams;

/**
 *
 * @author evans
 */
public class VellumLocalHttpServerHandler implements HttpHandler {

    final Logr logger = LogrFactory.getLogger(WebHandler.class);
    final VellumLocalHttpServer server;
    final VellumLocalHttpServerConfig config;
    
    public VellumLocalHttpServerHandler(VellumLocalHttpServer app) {
        this.server = app;
        this.config = app.getConfig();
    }

    public void init() throws IOException {
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        if (httpExchange.getRequestURI().getPath().endsWith(".png")) {
            httpExchange.getResponseHeaders().set("Content-type", "image/png");
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
            path = config.getRootFile();
        }
        if (!path.startsWith("/bootstrap")) {
            logger.info("path", path);
        }
        try {
            FileInputStream inputStream = new FileInputStream(config.getRootDir() + '/' + path);
            byte[] bytes = Streams.readBytes(inputStream);
            logger.info("path", path, bytes.length);
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            httpExchange.getResponseBody().write(bytes);        
        } catch (Exception e) {
            logger.warn(e);
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
        }
        httpExchange.close();
    }
}
