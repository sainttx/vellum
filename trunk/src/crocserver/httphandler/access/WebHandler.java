/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.CrocApp;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Streams;

/**
 *
 * @author evans
 */
public class WebHandler implements HttpHandler {

    Logr logger = LogrFactory.getLogger(WebHandler.class);
    CrocApp app;
    Map<String, byte[]> cache = new HashMap();
    
    public WebHandler(CrocApp app) {
        super();
        this.app = app;
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
            path = app.getHomePage();
        }
        String resourceName = "/crocserver/web" + path;
        if (!path.startsWith("/bootstrap")) {
            logger.info("resource", resourceName);
        }
        try {
            byte[] bytes = cache.get(path);
            if (bytes == null) {
                InputStream resourceStream = getClass().getResourceAsStream(resourceName);
                if (path.endsWith("/bindex.html")) {
                    StringBuilder html = Streams.readStringBuilder(resourceStream);
                    replace(html);
                    bytes = html.toString().getBytes();
                } else {
                    bytes = Streams.readBytes(resourceStream);
                }
                cache.put(path, bytes);
            }
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            httpExchange.getResponseBody().write(bytes);                
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            logger.warn(e);
        }
        httpExchange.close();
    }

    private void replace(StringBuilder html) {
        replace(html, "${loginUrl}", app.getGoogleApi().getLoginUrl());
        replace(html, "${clientId}", app.getGoogleApi().getClientId());
        replace(html, "${redirectUrl}", app.getGoogleApi().getRedirectUrl());
        replace(html, "${serverUrl}", app.getServerUrl());
    }
    
    private void replace(StringBuilder html, String pattern, String string) {
        int index = html.indexOf(pattern);
        if (index >= 0) {
            html.replace(index, index + pattern.length(), string);
        }
    }
    
}
