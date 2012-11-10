/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.CrocApp;
import crocserver.httphandler.secure.GenP12Handler;
import crocserver.httphandler.secure.SecureHomeHandler;
import java.io.IOException;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import crocserver.storage.common.CrocStorage;

/**
 *
 * @author evans
 */
public class AccessHttpHandler implements HttpHandler {

    Logr logger = LogrFactory.getLogger(AccessHttpHandler.class);
    CrocApp app;
    CrocStorage storage;
    WebHandler webHandler;
    
    public AccessHttpHandler(CrocApp app) {
        this.app = app;
        storage = app.getStorage();
        webHandler = new WebHandler(app);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if (!handlePath(httpExchange)) {
            webHandler.handle(httpExchange);
        }
    }

    public boolean handlePath(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        logger.info("path", path);
        if (path.startsWith("/oauth")) {
            new OAuthCallbackHandler(app).handle(httpExchange);
        } else if (path.startsWith("/echo")) {
            new EchoHandler(app).handle(httpExchange);
        } else if (path.startsWith("/admin")) {
            new SecureHomeHandler(app).handle(httpExchange);
        } else if (path.startsWith("/enroll/user/")) {
            new EnrollUserHandler(app).handle(httpExchange);
        } else if (path.startsWith("/enroll/org/")) {
            new EnrollOrgHandler(app).handle(httpExchange);
        } else if (path.startsWith("/enroll/service/")) {
            new EnrollServiceHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/get/cert/")) {
            new GetCertHandler(storage).handle(httpExchange);
        } else if (path.equals("/login")) {
            new LoginHandler(app).handle(httpExchange);
        } else if (path.startsWith("/gen/p12/")) {
            new GenP12Handler(app).handle(httpExchange);
        } else if (path.startsWith("/sign/cert/")) {
            new SignCertHandler(app).handle(httpExchange);
        } else if (path.startsWith("/view/user/")) {
            new ViewUserHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/view/cert/")) {
            new ViewCertHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/view/serviceRecord/")) {
            new ViewServiceRecordHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/view/org/")) {
            new ViewOrgHandler(storage).handle(httpExchange);
        } else if (path.startsWith("/storage")) {
            new StoragePageHandler(storage).handle(httpExchange);
        } else {
            return false;
        }
        return true;
    }
    
}
