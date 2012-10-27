/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import bizstat.entity.ServiceRecord;
import bizstat.enumtype.ServiceStatus;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.httpserver.HttpExchangeInfo;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.storage.StorageException;
import vellum.util.Streams;
import crocserver.storage.CrocStorage;

/**
 *
 * @author evans
 */
public class GenKeyHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    CrocStorage storage;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    PrintStream out;

    public GenKeyHandler(CrocStorage storage) {
        super();
        this.storage = storage;
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        httpExchange.getResponseHeaders().set("Content-type", "text/plain");
        String text = Streams.readString(httpExchange.getRequestBody());
        String[] args = httpExchangeInfo.splitPath();
        out = new PrintStream(httpExchange.getResponseBody());
        try {
            String hostName = httpExchangeInfo.getPathString(1, "none");
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            out.println("OK " + getClass().getSimpleName());
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            e.printStackTrace(out);
            e.printStackTrace(System.err);
            out.println("ERROR " + e.getMessage());
        }
        httpExchange.close();
    }

    private void check(String hostName, String serviceName, String notifyName, String text) throws StorageException, SQLException {
        storage.getServiceRecordStorage().find(hostName, serviceName);        
    }
    
}
