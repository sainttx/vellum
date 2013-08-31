/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 * 
 */
package bizstat.server;

import bizstat.entity.ServiceRecord;
import java.util.Comparator;
import java.util.TreeSet;

/**
 *
 * @author evan.summers
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
