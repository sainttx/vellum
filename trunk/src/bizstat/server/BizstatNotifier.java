/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012, Evan Summers
 */
package bizstat.server;

import crocserver.storage.servicerecord.ServiceRecord;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import bizstat.entity.*;
import java.util.*;

/**
 *
 * @author evan
 */
public class BizstatNotifier implements Runnable {

    Logr logger = LogrFactory.getLogger(BizstatNotifier.class);
    BizstatServer server;
    Map<Contact, BizstatContactNotifier> contactNotifierMap = new HashMap();
    TreeSet<HostServiceStatus> notifyStatusSet = new TreeSet(new HostServiceStatusKeyComparator());
    
    public BizstatNotifier(BizstatServer server) {
        this.server = server;
    }
    
    @Override
    public void run() {
        for (HostServiceStatus status : server.statusMap.values()) {
            if (status.isNotify()) {
                notifyStatusSet.add(status);
            }
        }
        if (notifyStatusSet.size() > 0) {
            server.notifiedMillis = System.currentTimeMillis();
            notifyContact();
        }
    }
        
    private void notifyContact() {
        logger.info("notifyStatus", notifyStatusSet.last());
        for (HostServiceStatus status : notifyStatusSet) {
            status.setNotifiedMillis(server.notifiedMillis);
            server.insert(status.getServiceRecord());
            notifyContact(status);
        }
        for (BizstatContactNotifier contactNotifier : contactNotifierMap.values()) {
            contactNotifier.run();
        }
    }
    
    private void notifyContact(HostServiceStatus status) {
        for (ContactGroup contactGroup : status.getContactGroupList()) {
            if (contactGroup.isEnabled()) {
                notifyStatus(contactGroup, status.getServiceRecord());
            }
        }
    }

    private void notifyStatus(ContactGroup contactGroup, ServiceRecord serviceRecord) {
        logger.info("notify", contactGroup, serviceRecord);
        for (Contact contact : contactGroup.getContactList()) {
            if (contact.isEnabled() && !server.isStopped()) {
                BizstatContactNotifier contactNotifier = contactNotifierMap.get(contact);
                if (contactNotifier == null) {
                    contactNotifier = new BizstatContactNotifier(server, contact);
                    contactNotifierMap.put(contact, contactNotifier);
                }
                contactNotifier.getserviceRecordList().add(serviceRecord);
            }
        }
    }
}
