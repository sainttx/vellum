/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 */
package bizstat.server;

import bizstat.entity.StatusInfo;
import java.util.Comparator;

/**
 *
 * @author evans
 */
public class StatusInfoKeyComparator implements Comparator<StatusInfo> {

    @Override
    public int compare(StatusInfo o1, StatusInfo o2) {
        return o1.getKey().compareTo(o2.getKey());
    }
}
