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

    String orgName;
    String hostName;
    String serviceName;
    String notifyName;
    String serviceText;
    
    public PostHandler(CrocStorage storage) {
        super();
        this.storage = storage;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        httpExchange.getResponseHeaders().set("Content-type", "text/plain");
        serviceText = Streams.readString(httpExchange.getRequestBody());
        out = new PrintStream(httpExchange.getResponseBody());
        if (httpExchangeInfo.getPathLength() == 5) {
            orgName = httpExchangeInfo.getPathString(1);
            hostName = httpExchangeInfo.getPathString(2);
            serviceName = httpExchangeInfo.getPathString(3);
            notifyName = httpExchangeInfo.getPathString(4);
            try {
                if (notifyName != null) {
                    check();
                }
                ServiceRecord serviceRecord = new ServiceRecord(hostName, serviceName, ServiceStatus.UNKNOWN, serviceText);
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
        } else {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            out.printf("ERROR\n");

        }
        httpExchange.close();
    }

    private void check() throws SQLException {
        Org org = storage.getOrgStorage().get(orgName);
        ServiceRecord serviceRecord = storage.getServiceRecordStorage().findLatest(org.getId(), hostName, serviceName);
        logger.info("last", serviceRecord);
    }
}
