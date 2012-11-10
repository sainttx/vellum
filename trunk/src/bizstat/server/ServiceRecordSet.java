/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012, Evan Summers
 * 
 */
package bizstat.server;

import crocserver.storage.servicerecord.ServiceRecord;
import java.util.Comparator;
import java.util.TreeSet;

/**
 *
 * @author evans
 */
public class ServiceRecordSet extends TreeSet<ServiceRecord> {

    public ServiceRecordSet() {
        super(new Comparator<ServiceRecord>() {

            @Override
            public int compare(ServiceRecord o1, ServiceRecord o2) {
                return o1.getHostServiceKey().compareTo(o2.getHostServiceKey());
            }
        });
    }
}
