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
import vellum.util.Streams;

/**
 *
 * @author evans
 */
public class PostSecretHandler implements HttpHandler {

    Logr logger = LogrFactory.getLogger(getClass());
    SaltApp app;
    SaltStorage storage;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;

    public PostSecretHandler(SaltApp app) {
        super();
        this.app = app;
    }
    String group;
    String name;
    String secretValue;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        if (httpExchangeInfo.getPathArgs().length < 3) {
            httpExchangeInfo.handleError();
        } else {
            group = httpExchangeInfo.getPathString(1);
            name = httpExchangeInfo.getPathString(2);
            secretValue = Streams.readString(httpExchange.getRequestBody());
            logger.info("handle", getClass().getSimpleName(), group, name, secretValue);
            try {
                handle();
            } catch (Exception e) {
                httpExchangeInfo.handleException(e);
            }
        }
        httpExchange.close();
    }

    private void handle() throws Exception {
        StringMap responseMap = new StringMap();
        Secret secret = app.getStorage().getSecretStorage().find(group, name);
        if (secret == null) {
            secret = new Secret();
            secret.setGroup(group);
            secret.setName(name);
            secret.setSecret(secretValue);
            app.getStorage().getSecretStorage().insert(secret);
            responseMap.put("action", "inserted");
        } else {
            secret.setSecret(secretValue);
            app.getStorage().getSecretStorage().update(secret);
            responseMap.put("action", "updated");
        }
        responseMap.putObject("id", secret.getId());
        String json = JsonStrings.buildJson(responseMap);
        httpExchangeInfo.sendJsonResponse(json);
        logger.info(json);
    }
}
