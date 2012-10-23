/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 */
package bizstat.server;

import bizstat.entity.ServiceRecord;
import java.util.Comparator;

/**
 *
 * @author evans
 */
public class StatusInfoServiceStatusComparator implements Comparator<ServiceRecord> {

    @Override
    public int compare(ServiceRecord o1, ServiceRecord o2) {
        return o1.getServiceStatus().compareTo(o2.getServiceStatus());
    }
}
