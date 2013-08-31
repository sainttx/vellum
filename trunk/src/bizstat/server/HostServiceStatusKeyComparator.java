/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 */
package bizstat.server;

import bizstat.entity.HostServiceStatus;
import java.util.Comparator;

/**
 *
 * @author evan.summers
 */
public class HostServiceStatusKeyComparator implements Comparator<HostServiceStatus> {

    @Override
    public int compare(HostServiceStatus o1, HostServiceStatus o2) {
        return o1.getHostServiceKey().compareTo(o2.getHostServiceKey());
    }
}
