/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012 Evan Summers, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2010 iPay (Pty) Ltd
 */
package crocserver.httphandler.secure;

import bizstat.enumtype.NotifyType;
import crocserver.storage.servicerecord.ServiceRecord;
import bizstat.enumtype.ServiceStatus;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.CrocApp;
import crocserver.httpserver.HttpExchangeInfo;
import crocserver.notify.ServiceRecordProcessor;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Streams;
import crocserver.storage.common.CrocStorage;
import crocserver.storage.org.Org;
import java.text.MessageFormat;
import vellum.datatype.Millis;

/**
 *
 * @author evans
 */
public class PostHandler implements HttpHandler {

    Logr logger = LogrFactory.getLogger(getClass());
    CrocApp app;
    CrocStorage storage;
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    PrintStream out;
    String orgName;
    String hostName;
    String serviceName;
    String notifyName;
    String serviceText;
    ServiceRecord currentRecord;
    ServiceStatus serviceStatus = ServiceStatus.UNKNOWN;
    NotifyType notifyType;

    public PostHandler(CrocApp app) {
        super();
        this.app = app;
        this.storage = app.getStorage();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        httpExchange.getResponseHeaders().set("Content-type", "text/plain");
        serviceText = Streams.readString(httpExchange.getRequestBody());
        out = new PrintStream(httpExchange.getResponseBody());
        if (httpExchangeInfo.getPathLength() >= 4) {
            orgName = httpExchangeInfo.getPathString(1);
            hostName = httpExchangeInfo.getPathString(2);
            serviceName = httpExchangeInfo.getPathString(3);
            notifyName = httpExchangeInfo.getPathString(4);
            try {
                Org org = storage.getOrgStorage().get(orgName);
                currentRecord = new ServiceRecord(hostName, serviceName);
                currentRecord.parseOutText(serviceText);
                ServiceRecordProcessor processor = new ServiceRecordProcessor(app);
                if (notifyName != null) {
                    notifyType = NotifyType.valueOf(notifyName);
                    ServiceRecord previousRecord = storage.getServiceRecordStorage().findLatest(org.getId(), hostName, serviceName);
                    logger.info("last", Millis.formatTimestamp(previousRecord.getTimestamp()));
                    processor.process(notifyType, previousRecord, currentRecord);
                    logger.info("notify", processor.isNotify());
                }
                storage.getServiceRecordStorage().insert(org, currentRecord);
                if (processor.isNotify()) {
                    app.sendAdminGtalkMessage(MessageFormat.format("@{0} CHANGED {1} https://croc.linuxd.org:8443/view/serviceRecord/{2}", 
                        currentRecord.getHostName(), currentRecord.getServiceName(), currentRecord.getId()));
                }
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
}
