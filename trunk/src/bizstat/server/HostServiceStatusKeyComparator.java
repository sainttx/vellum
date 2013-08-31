/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
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
