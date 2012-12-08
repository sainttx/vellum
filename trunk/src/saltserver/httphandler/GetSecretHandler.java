/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */
package saltserver.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.JsonStrings;
import vellum.httpserver.HttpExchangeInfo;
import java.io.IOException;
import saltserver.app.SecretApp;
import saltserver.app.SecretAppStorage;
import saltserver.storage.secret.SecretValue;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.parameter.StringMap;

/**
 *
 * @author evans
 */
public class GetSecretHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    SecretApp app;
    SecretAppStorage storage;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    
    public GetSecretHandler(SecretApp app) {
        super();
        this.app = app;
    }

    String group;
    String name;
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        logger.info("handle", getClass().getSimpleName());
        if (httpExchangeInfo.getPathArgs().length < 3) {
            httpExchangeInfo.handleError();
        } else {
            group = httpExchangeInfo.getPathString(1);
            name = httpExchangeInfo.getPathString(2);
            try {
                handle();
            } catch (Exception e) {
                httpExchangeInfo.handleException(e);
            }
        }
        httpExchange.close();
    }
    
    private void handle() throws Exception {
        SecretValue secret = app.getStorage().getSecretStorage().get(group, name);
        StringMap responseMap = new StringMap();
        responseMap.putObject("id", secret.getId());
        responseMap.put("secret", secret.getSecret());
        String json = JsonStrings.buildJson(responseMap);
        httpExchangeInfo.sendJsonResponse(json);
        logger.info(json);
    }
}
