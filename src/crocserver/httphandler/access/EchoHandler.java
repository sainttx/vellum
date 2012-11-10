/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012 Evan Summers, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2010 iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.CrocApp;
import crocserver.httpserver.HttpExchangeInfo;
import java.io.IOException;
import java.io.PrintStream;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Lists;

/**
 *
 * @author evans
 */
public class EchoHandler implements HttpHandler {

    Logr logger = LogrFactory.getLogger(getClass());
    CrocApp app;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    PrintStream out;

    public EchoHandler(CrocApp app) {
        super();
        this.app = app;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        logger.info("handle", getClass().getSimpleName());
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        httpExchange.getResponseHeaders().set("Set-Cookie", "testCookie=test");
        httpExchangeInfo.sendResponse("text/plain", true);
        PrintStream out = new PrintStream(httpExchange.getResponseBody());
        out.println(httpExchange.getRequestURI().toString());
        out.println(httpExchangeInfo.parseFirstRequestHeader("Cookie")[0]);
        Headers reqHeaders = httpExchange.getRequestHeaders();
        for (String key : reqHeaders.keySet()) {           
            out.printf("request header %s: %d: %s\n", key, reqHeaders.get(key).size(), reqHeaders.get(key).get(0));
        }
        Headers resHeaders = httpExchange.getResponseHeaders();
        for (String key : resHeaders.keySet()) {            
            out.printf("response header %s: %s\n", key, resHeaders.get(key));
        }
        httpExchange.close();
    }
}
