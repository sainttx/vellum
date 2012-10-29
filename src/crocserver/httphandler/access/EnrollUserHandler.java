/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.httpserver.HttpExchangeInfo;
import crocserver.storage.adminuser.AdminRole;
import crocserver.storage.adminuser.User;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.storage.StorageException;
import crocserver.storage.common.CrocStorage;
import crocserver.storage.org.Org;
import java.util.Date;
import vellum.format.ListFormats;

/**
 *
 * @author evans
 */
public class EnrollUserHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    CrocStorage storage;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    PrintStream out;

    String userName;
    
    public EnrollUserHandler(CrocStorage storage) {
        super();
        this.storage = storage;
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        httpExchange.getResponseHeaders().set("Content-type", "text/plain");
        out = new PrintStream(httpExchange.getResponseBody());
        if (httpExchangeInfo.getPathLength() < 3) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            out.printf("ERROR %s\n", httpExchangeInfo.getPath());
        } else {
            userName = httpExchangeInfo.getPathString(2);
            try {
                insert();
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            } catch (Exception e) {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                e.printStackTrace(out);
                e.printStackTrace(System.err);
                out.printf("ERROR %s\n", e.getMessage());
            }
        }
        httpExchange.close();
    }

    private void insert() throws StorageException, SQLException {
        User user = new User(userName);
        user.setDisplayName(httpExchangeInfo.getParameterMap().get("userDisplayName"));
        user.setEmail(httpExchangeInfo.getParameterMap().get("email"));
        user.setRole(AdminRole.DEFAULT);
        storage.getUserStorage().insert(user);
        out.printf("OK %s\n", ListFormats.displayFormatter.formatArgs(
                getClass().getName(), userName, httpExchangeInfo.getParameterMap()
                ));
    }
    
}
