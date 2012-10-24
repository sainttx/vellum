/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package venigmon.httpserver;

import bizstat.entity.ServiceRecord;
import bizstat.server.BizstatMessageBuilder;
import vellum.datatype.Millis;
import vellum.html.TablePrinter;
import venigmon.storage.VenigmonStorage;

/**
 *
 * @author evans
 */
public class ViewServiceRecordPageHandler extends AbstractPageHandler {
    VenigmonStorage storage;

    public ViewServiceRecordPageHandler(VenigmonStorage storage) {
        super();
        this.storage = storage;
    }

    @Override
    protected void handle() throws Exception {
       TablePrinter p = new TablePrinter(out);
       Long id = Long.parseLong(pathArgs[2]);
       out.printf("<div class='menuBarDiv'><a href='/'>Home</a></div>\n");
       out.printf("<span class='pageTitle'>ServiceRecord %d</span>", id);
       ServiceRecord serviceRecord = storage.getServiceRecordStorage().find(id);
       p.tableDiv("resultSet");
       p.thead();
       p.theadClose();
       p.tbody();
       p.trhd("Id", serviceRecord.getId());
       p.trhd("Time", Millis.formatTime(serviceRecord.getTimestamp()));
       p.trhd("Host", serviceRecord.getHostName());
       p.trhd("Service", serviceRecord.getServiceName());
       p.trhd("Status", serviceRecord.getServiceStatus());
       p.tbodyClose();
       p.tableDivClose();
       p.pre(serviceRecord.getOutText());
    }    
}
