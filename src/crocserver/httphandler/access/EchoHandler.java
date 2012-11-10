/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, (c) Copyright 2010 iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.CrocApp;
import crocserver.app.CrocSecurity;
import crocserver.app.GoogleUserInfo;
import crocserver.httpserver.HttpExchangeInfo;
import crocserver.storage.adminuser.AdminRole;
import crocserver.storage.adminuser.AdminUser;
import java.io.IOException;
import java.io.PrintStream;
import vellum.html.HtmlPrinter;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Streams;
import vellum.util.Strings;

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
        httpExchangeInfo.setResponse("text/plain", true);
        PrintStream out = new PrintStream(httpExchange.getResponseBody());
        out.println(httpExchange.getRequestURI().toString());
        httpExchange.close();
    }
}
