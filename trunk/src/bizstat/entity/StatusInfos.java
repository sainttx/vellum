/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package bizstat.entity;

import java.util.Collection;

/**
 *
 * @author evans
 */
public class StatusInfos {

    public static String toString(Collection<StatusInfo> collection) {
        return String.format("%d %s", collection.size(), collection.iterator().next());
    }

    
}
