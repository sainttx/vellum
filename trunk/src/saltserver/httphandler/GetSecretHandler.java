/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */
package saltserver.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.JsonStrings;
import vellum.httpserver.HttpExchangeInfo;
import java.io.IOException;
import saltserver.app.SaltApp;
import saltserver.app.SaltStorage;
import saltserver.storage.secret.Secret;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.parameter.StringMap;

/**
 *
 * @author evans
 */
public class GetSecretHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    SaltApp app;
    SaltStorage storage;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    
    public GetSecretHandler(SaltApp app) {
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
        Secret secret = app.getStorage().getSecretStorage().get(group, name);
        StringMap responseMap = new StringMap();
        responseMap.putObject("id", secret.getId());
        responseMap.put("secret", secret.getSecret());
        String json = JsonStrings.buildJson(responseMap);
        httpExchangeInfo.sendJsonResponse(json);
        logger.info(json);
    }
}
