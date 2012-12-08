/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */
package saltserver.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import vellum.httpserver.HttpExchangeInfo;
import java.io.IOException;
import saltserver.app.SecretApp;
import saltserver.app.SecretAppStorage;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evans
 */
public class SecretShutdownHandler implements HttpHandler {

    Logr logger = LogrFactory.getLogger(getClass());
    SecretApp app;
    SecretAppStorage storage;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;

    public SecretShutdownHandler(SecretApp app) {
        super();
        this.app = app;
        this.storage = app.getStorage();
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
            httpExchangeInfo.handleException(e);
            httpExchange.close();
        }
    }
}
