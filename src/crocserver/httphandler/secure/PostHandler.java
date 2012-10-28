/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.secure;

import crocserver.storage.servicerecord.ServiceRecord;
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
import crocserver.storage.org.Org;

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
        String[] args = httpExchangeInfo.getPathArgs();
        out = new PrintStream(httpExchange.getResponseBody());
        try {
            String orgName = httpExchangeInfo.getPathString(1);
            String hostName = httpExchangeInfo.getPathString(2);
            String serviceName = httpExchangeInfo.getPathString(3);
            String notifyName = httpExchangeInfo.getPathString(3);
            if (notifyName != null) {
                check(hostName, serviceName, notifyName, text);
            }
            ServiceRecord serviceRecord = new ServiceRecord(hostName, serviceName, ServiceStatus.UNKNOWN, text);
            Org org = storage.getOrgStorage().get(orgName);
            storage.getServiceRecordStorage().insert(org, serviceRecord);
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            out.println("OK " + getClass().getSimpleName());
        } catch (Exception e) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            e.printStackTrace(out);
            e.printStackTrace(System.err);
            out.printf("ERROR %s\n", e.getMessage());
        }
        httpExchange.close();
    }

    private void check(String hostName, String serviceName, String notifyName, String text) throws StorageException, SQLException {
        storage.getServiceRecordStorage().find(hostName, serviceName);

    }

    private void store(String hostName, String serviceName, String text) throws StorageException, SQLException {
        logger.info("store", hostName, serviceName, text);
    }
}
