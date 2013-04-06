/*
 * Apache Software License 2.0, (c) Copyright 2012 Evan Summers, 2010 iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import crocserver.storage.servicerecord.ServiceRecord;
import crocserver.httphandler.common.AbstractPageHandler;
import vellum.datatype.Millis;
import vellum.html.HtmlPrinter;
import crocserver.storage.common.CrocStorage;

/**
 *
 * @author evans
 */
public class ViewServiceRecordHandler extends AbstractPageHandler {
    CrocStorage storage;

    public ViewServiceRecordHandler(CrocStorage storage) {
        super();
        this.storage = storage;
    }

    @Override
    protected void handle() throws Exception {
       HtmlPrinter p = new HtmlPrinter(out);
       Long id = Long.parseLong(pathArgs[1]);
       p.div("menuBarDiv");
       p.aClosed("/", "Home");
       p.divClose();
       p.span("pageTitle", String.format("ServiceRecord %d", id));
       ServiceRecord serviceRecord = storage.getServiceRecordStorage().find(id);
       p.tableDiv("resultSet");
       p.thead();
       p.theadClose();
       p.tbody();
       p.trhd("Id", serviceRecord.getId());
       p.trhd("Host", serviceRecord.getCertName());
       p.trhd("Service", serviceRecord.getServiceName());
       p.trhd("Timestamp", Millis.format(serviceRecord.getTimestamp()));
       p.trhd("Status", serviceRecord.getServiceStatus());
       p.tbodyClose();
       p.tableDivClose();
       p.pre(serviceRecord.getOutText());
    }    
}
