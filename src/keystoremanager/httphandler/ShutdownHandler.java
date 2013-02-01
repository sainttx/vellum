/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */
package keystoremanager.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import vellum.httpserver.HttpExchangeInfo;
import java.io.IOException;
import keystoremanager.app.MantraApp;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evans
 */
public class ShutdownHandler implements HttpHandler {

    Logr logger = LogrFactory.getLogger(getClass());
    MantraApp app;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;

    public ShutdownHandler(MantraApp app) {
        super();
        this.app = app;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        logger.info("handle", getClass().getSimpleName(), httpExchangeInfo.getPath());
        try {
            httpExchangeInfo.sendResponse("text/plain", true);
            httpExchangeInfo.getPrintStream().printf("OK %s\n", httpExchangeInfo.getPath());
            httpExchange.close();
            app.stop();
        } catch (Exception e) {
            httpExchangeInfo.handleError(e);
            httpExchange.close();
        }
    }
}
