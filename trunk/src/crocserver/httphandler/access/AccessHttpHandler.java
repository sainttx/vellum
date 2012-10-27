/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import crocserver.storage.CrocStorage;

/**
 *
 * @author evans
 */
public class AccessHttpHandler implements HttpHandler {

    Logr logger = LogrFactory.getLogger(AccessHttpHandler.class);
    CrocStorage storage;

    public AccessHttpHandler(CrocStorage storage) {
        this.storage = storage;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        logger.info("path", path);
        if (path.startsWith("/register/")) {
            new RegisterHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/storage/")) {
            new StoragePageHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/view/serviceRecord/")) {
            new ViewServiceRecordPageHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/genKey/")) {
            new GenKeyHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/publicKey/")) {
            new GetPublicKeyHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/certReq/")) {
            new CertReqHandler(storage).handle(httpExchange);
        } else {
            new AccessHomePageHandler(storage).handle(httpExchange);
        }
    }
}
