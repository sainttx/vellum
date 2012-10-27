/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package crocserver.httphandler.access;

import bizstat.entity.ServiceRecord;
import crocserver.httphandler.common.AbstractPageHandler;
import vellum.datatype.Millis;
import vellum.html.HtmlPrinter;
import crocserver.storage.CrocStorage;

/**
 *
 * @author evans
 */
public class ViewServiceRecordPageHandler extends AbstractPageHandler {
    CrocStorage storage;

    public ViewServiceRecordPageHandler(CrocStorage storage) {
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
       p.spanf("pageTitle", "ServiceRecord %d", id);
       ServiceRecord serviceRecord = storage.getServiceRecordStorage().find(id);
       p.tableDiv("resultSet");
       p.thead();
       p._thead();
       p.tbody();
       p.trhd("Id", serviceRecord.getId());
       p.trhd("Time", Millis.formatTime(serviceRecord.getTimestamp()));
       p.trhd("Host", serviceRecord.getHostName());
       p.trhd("Service", serviceRecord.getServiceName());
       p.trhd("Status", serviceRecord.getServiceStatus());
       p._tbody();
       p._tableDiv();
       p.pre(serviceRecord.getOutText());
    }    
}
