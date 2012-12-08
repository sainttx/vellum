/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers
 */
package saltserver.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import vellum.httpserver.HttpExchangeInfo;
import java.io.IOException;
import java.util.Arrays;
import saltserver.app.SecretApp;
import saltserver.app.SecretAppStorage;
import saltserver.storage.secret.SecretRecord;
import vellum.crypto.Base64;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

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
    byte[] iv;
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        logger.info("handle", getClass().getSimpleName());
        if (httpExchangeInfo.getPathArgs().length < 4) {
            httpExchangeInfo.handleError();
        } else {
            group = httpExchangeInfo.getPathString(1);
            name = httpExchangeInfo.getPathString(2);
            iv = Base64.decode(httpExchangeInfo.getPathString(3));
            try {
                handle();
            } catch (Exception e) {
                httpExchangeInfo.handleException(e);
            }
        }
        httpExchange.close();
    }
    
    private void handle() throws Exception {
        SecretRecord secret = app.getStorage().getSecretStorage().get(group, name);
        byte[] secretBytes = app.getCipher().decrypt(Base64.decode(secret.getSecret()), iv);
        httpExchangeInfo.sendResponse("application/octet-stream", true);
        httpExchange.getResponseBody().write(secretBytes);
        Arrays.fill(secretBytes, Byte.MIN_VALUE);
    }
}
