/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package venigmon.httpserver;

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
import venigmon.storage.VenigmonStorage;

/**
 *
 * @author evans
 */
public class PostHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    VenigmonStorage storage;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    PrintStream out;

    public PostHandler(VenigmonStorage storage) {
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
            store(args[1], args[2], text);
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
    
    private void store(String hostName, String serviceName, String text) throws StorageException, SQLException {
        logger.info("store", hostName, serviceName, text);
        Host host = new Host(hostName);
        Service service = new Service(serviceName);
        ServiceRecord serviceRecord = new ServiceRecord(host, service, ServiceStatus.UNKNOWN, System.currentTimeMillis(), text);
        storage.getServiceRecordStorage().insert(serviceRecord);
    }

}
