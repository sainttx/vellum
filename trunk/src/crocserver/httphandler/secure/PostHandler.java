/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */
package crocserver.httphandler.secure;

import bizstat.enumtype.NotifyType;
import crocserver.storage.servicerecord.ServiceRecord;
import bizstat.enumtype.ServiceStatus;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.app.CrocApp;
import crocserver.exception.CrocError;
import crocserver.exception.CrocExceptionType;
import vellum.httpserver.HttpExchangeInfo;
import crocserver.notify.ServiceRecordProcessor;
import java.io.IOException;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Streams;
import crocserver.storage.common.CrocStorage;
import crocserver.storage.org.Org;
import java.text.MessageFormat;
import vellum.datatype.Millis;
import vellum.parameter.StringMap;

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
    String orgName;
    String hostName;
    String certName;
    String serviceName;
    String notifyName;
    String serviceText;
    ServiceRecord newRecord;
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
        if (httpExchangeInfo.getPathLength() == 6) {
            orgName = httpExchangeInfo.getPathString(1);
            hostName = httpExchangeInfo.getPathString(2);
            certName = httpExchangeInfo.getPathString(3);
            serviceName = httpExchangeInfo.getPathString(4);
            notifyName = httpExchangeInfo.getPathString(5);
            try {
                handle();
            } catch (Exception e) {
                httpExchangeInfo.handleError(e);
            }
        } else {
            httpExchangeInfo.handleError(new CrocError(CrocExceptionType.INVALID_ARGS, httpExchangeInfo.getPathLength()));
        }
        httpExchange.close();
    }
    
    private void handle() throws Exception {
        Org org = storage.getOrgStorage().get(orgName);
        newRecord = new ServiceRecord(hostName, certName);
        newRecord.parseOutText(serviceText);
        ServiceRecordProcessor processor = new ServiceRecordProcessor(app);
        if (notifyName != null) {
            notifyType = NotifyType.valueOf(notifyName);
            ServiceRecord previousRecord = storage.getServiceRecordStorage().findLatest(org.getId(), hostName, certName);
            logger.info("previous", previousRecord);
            processor.process(notifyType, previousRecord, newRecord);
            logger.info("notify", processor.isNotify());
        }
        storage.getServiceRecordStorage().insert(org, newRecord);
        if (processor.isNotify()) {
            app.sendAdminGtalkMessage(MessageFormat.format("@{0} CHANGED {1} {2}/view/serviceRecord/{3}",
                    newRecord.getHostName(), newRecord.getServiceName(), app.getSecureUrl(), newRecord.getId()));
        }
        StringMap responseMap = new StringMap();
        httpExchangeInfo.write(responseMap);
    }
}
