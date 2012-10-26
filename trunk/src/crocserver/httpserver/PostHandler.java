/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httpserver;

import bizstat.entity.Host;
import bizstat.entity.Service;
import bizstat.entity.ServiceRecord;
import bizstat.enumtype.ServiceStatus;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
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
public class PostHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    CrocStorage storage;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    PrintStream out;

    public PostHandler(CrocStorage storage) {
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
            String serviceName = httpExchangeInfo.getPathString(2, "none");
            String notifyName = httpExchangeInfo.getPathString(3, null);
            if (notifyName != null) {
                check(hostName, serviceName, notifyName, text);
            }
            store(hostName, serviceName, text);
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
    
    private void store(String hostName, String serviceName, String text) throws StorageException, SQLException {
        logger.info("store", hostName, serviceName, text);
        ServiceRecord serviceRecord = new ServiceRecord(hostName, serviceName, ServiceStatus.UNKNOWN, System.currentTimeMillis(), text);
        storage.getServiceRecordStorage().insert(serviceRecord);
    }
}
