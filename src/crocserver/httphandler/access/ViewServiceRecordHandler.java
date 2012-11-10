/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012 Evan Summers, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2010 iPay (Pty) Ltd
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
       Long id = Long.parseLong(pathArgs[2]);
       p.div("menuBarDiv");
       p.a_("/", "Home");
       p._div();
       p.span("pageTitle", String.format("ServiceRecord %d", id));
       ServiceRecord serviceRecord = storage.getServiceRecordStorage().find(id);
       p.tableDiv("resultSet");
       p.thead();
       p._thead();
       p.tbody();
       p.trhd("Id", serviceRecord.getId());
       p.trhd("Host", serviceRecord.getHostName());
       p.trhd("Service", serviceRecord.getServiceName());
       p.trhd("Timestamp", Millis.formatTimestamp(serviceRecord.getTimestamp()));
       p.trhd("Status", serviceRecord.getServiceStatus());
       p._tbody();
       p._tableDiv();
       p.pre(serviceRecord.getOutText());
    }    
}
