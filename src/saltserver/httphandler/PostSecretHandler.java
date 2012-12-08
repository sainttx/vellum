/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */
package saltserver.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.JsonStrings;
import vellum.httpserver.HttpExchangeInfo;
import java.io.IOException;
import java.util.Arrays;
import saltserver.app.SecretApp;
import saltserver.app.SecretAppStorage;
import saltserver.storage.secret.SecretRecord;
import vellum.crypto.Base64;
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
    SecretApp app;
    SecretAppStorage storage;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;

    public PostSecretHandler(SecretApp app) {
        super();
        this.app = app;
    }
    String group;
    String name;
    byte[] iv;
    byte[] secretBytes;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        if (httpExchangeInfo.getPathArgs().length < 4) {
            httpExchangeInfo.handleError();
        } else {
            group = httpExchangeInfo.getPathString(1);
            name = httpExchangeInfo.getPathString(2);
            iv = Base64.decode(httpExchangeInfo.getPathString(3));
            secretBytes = Streams.readBytes(httpExchange.getRequestBody());
            try {
                logger.info("handle", getClass().getSimpleName(), group, name, iv);
                handle();
            } catch (Exception e) {
                httpExchangeInfo.handleException(e);
            } finally {
                Arrays.fill(secretBytes, Byte.MIN_VALUE);
            }
        }
        httpExchange.close();
    }

    private void handle() throws Exception {
        StringMap responseMap = new StringMap();
        SecretRecord secret = app.getStorage().getSecretStorage().find(group, name);
        String encodedSecret = Base64.encode(app.getCipher().encrypt(secretBytes, iv));
        if (secret == null) {
            secret = new SecretRecord();
            secret.setGroup(group);
            secret.setName(name);
            secret.setSecret(encodedSecret);
            app.getStorage().getSecretStorage().insert(secret);
            responseMap.put("action", "inserted");
        } else {
            secret.setSecret(encodedSecret);
            app.getStorage().getSecretStorage().update(secret);
            responseMap.put("action", "updated");
        }
        responseMap.putObject("id", secret.getId());
        responseMap.putObject("encoded", encodedSecret);
        String json = JsonStrings.buildJson(responseMap);
        httpExchangeInfo.sendResponse("text/json", true);
        httpExchangeInfo.getPrintStream().println(json);
        logger.info(json);
    }
}
