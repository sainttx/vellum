/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package bizstat.server;

import vellum.datatype.Milli;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import bizstat.entity.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author evan
 */
public class BizstatContactNotifier implements Runnable {

    Logr logger = LogrFactory.getLogger(BizstatContactNotifier.class);
    BizstatServer server;
    Contact contact;
    Date notifyTime = null;
    List<ServiceRecord> serviceRecordList = new ArrayList();
    
    public BizstatContactNotifier(BizstatServer server, Contact contact) {
        this.server = server;
        this.contact = contact;
    }

    public List<ServiceRecord> getServiceRecordList() {
        return serviceRecordList;
    }
    
    @Override
    public void run() {
        if (!contact.isEnabled()) {
        } else if (!Milli.isElapsed(notifyTime, server.config.notifyMillis)) {
            logger.warn("notify ignore", notifyTime);
        } else {
            notifyTime = new Date();
            new BizstatMessenger(server, contact, serviceRecordList).send();
        }
    }
 
    public static boolean verifyEmail(String email) {
        if (email == null) {
            return false;
        }
        if (email.indexOf("@") > 0) {
            return true;
        }
        return false;
    }
}