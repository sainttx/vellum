/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 */
package bizstat.server;

import bizstat.entity.HostServiceStatus;
import java.util.Comparator;

/**
 *
 * @author evans
 */
public class HostServiceStatusKeyComparator implements Comparator<HostServiceStatus> {

    @Override
    public int compare(HostServiceStatus o1, HostServiceStatus o2) {
        return o1.getHostServiceKey().compareTo(o2.getHostServiceKey());
    }
}
