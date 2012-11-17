/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.CrocApp;
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
        HttpHandler handler = getHandler(httpExchange);
        if (handler == null) {
            handler = webHandler;
        }
        handler.handle(httpExchange);
    }

    public HttpHandler getHandler(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        logger.info("path", path);
        if (path.startsWith("/oauth")) {
            return new OAuthCallbackHandler(app);
        } else if (path.startsWith("/echo")) {
            return new EchoHandler(app);
        } else if (path.startsWith("/admin")) {
            return new SecureHomeHandler(app);
        } else if (path.startsWith("/enrollUser/")) {
            return new EnrollUserHandler(app);
        } else if (path.startsWith("/enrollOrg/")) {
            return new EnrollOrgHandler(app);
        } else if (path.startsWith("/enrollService/")) {
            return new EnrollServiceHandler(storage);
        } else if (path.startsWith("/getCert/")) {
            return new GetCertHandler(storage);
        } else if (path.equals("/login")) {
            return new LoginHandler(app);
        } else if (path.equals("/logout")) {
            return new LogoutHandler(app);
        } else if (path.equals("/genKey")) {
            return new GenKeyP12Handler(app);
        } else if (path.startsWith("/signCert/")) {
            return new SignCertHandler(app);
        } else if (path.startsWith("/viewUser/")) {
            return new ViewUserHandler(storage);
        } else if (path.startsWith("/viewCert/")) {
            return new ViewCertHandler(storage);
        } else if (path.startsWith("/viewService/")) {
            return new ViewServiceHandler(storage);
        } else if (path.startsWith("/viewServiceRecord/")) {
            return new ViewServiceRecordHandler(storage);
        } else if (path.startsWith("/viewOrg/")) {
            return new ViewOrgHandler(storage);
        } else if (path.startsWith("/storage")) {
            return new StoragePageHandler(storage);
        }
        return null;
    }        
}
