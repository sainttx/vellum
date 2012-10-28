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
        if (path.startsWith("/enroll/user/")) {
            new EnrollUserHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/enroll/org/")) {
            new EnrollOrgHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/enroll/service/")) {
            new EnrollServiceHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/get/serviceCert/")) {
            new GetServiceCertHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/sign/serviceCert/")) {
            new SignServiceCertHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/view/user/")) {
            new ViewUserHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/view/serviceCert/")) {
            new ViewServiceCertHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/view/serviceRecord/")) {
            new ViewServiceRecordHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/view/org/")) {
            new ViewOrgHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/storage")) {
            new StoragePageHandler(storage).handle(httpExchange);
        } else {
            new AccessHomeHandler(storage).handle(httpExchange);
        }
    }
}
