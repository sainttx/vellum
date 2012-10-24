/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package bizstat.server;

import bizstat.entity.ServiceRecord;
import java.util.Comparator;
import java.util.TreeSet;

/**
 *
 * @author evans
 */
public class StatusInfoSet extends TreeSet<ServiceRecord> {

    public StatusInfoSet() {
        super(new Comparator<ServiceRecord>() {

            @Override
            public int compare(ServiceRecord o1, ServiceRecord o2) {
                return o1.getHostServiceKey().compareTo(o2.getHostServiceKey());
            }
        });
    }
}
