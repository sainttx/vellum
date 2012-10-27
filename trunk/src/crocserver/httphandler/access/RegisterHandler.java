/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.httpserver.HttpExchangeInfo;
import crocserver.storage.adminuser.AdminRole;
import crocserver.storage.adminuser.AdminUser;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.storage.StorageException;
import crocserver.storage.CrocStorage;
import java.util.Date;
import vellum.format.ListFormats;

/**
 *
 * @author evans
 */
public class RegisterHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    CrocStorage storage;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    PrintStream out;

    public RegisterHandler(CrocStorage storage) {
        super();
        this.storage = storage;
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        httpExchange.getResponseHeaders().set("Content-type", "text/plain");
        out = new PrintStream(httpExchange.getResponseBody());
        String username = httpExchangeInfo.getPathString(1, null);
        if (username != null) {
            try {
                insert(username);
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            } catch (Exception e) {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                e.printStackTrace(out);
                e.printStackTrace(System.err);
                out.printf("ERROR %s\n", e.getMessage());
            }
        } else {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            out.printf("ERROR %s\n", httpExchangeInfo.getPath());
        }
        httpExchange.close();
    }

    private void insert(String username) throws StorageException, SQLException {
        AdminUser adminUser = new AdminUser(username, true);
        String displayName = httpExchangeInfo.getParameterMap().get("displayName");
        if (displayName != null) {
            displayName.replace('_', ' ');
            adminUser.setDisplayName(displayName);
        }
        String email = httpExchangeInfo.getParameterMap().get("address");
        if (email != null) {
            adminUser.setEmail(email);
        }
        adminUser.setInserted(new Date());
        adminUser.setRole(AdminRole.DEFAULT);
        storage.getAdminUserStorage().insert(adminUser);
        out.printf("OK %s\n", ListFormats.displayFormatter.formatArgs(
                getClass().getName(), username, displayName, email, httpExchangeInfo.getParameterMap()
                ));
    }
    
}
