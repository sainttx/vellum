/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 * 
 */
package vellum.entity;

import java.util.List;

/**
 *
 * @author evan.summers
 */
public interface HasChildList<T> {
    public List<T> getChildList();
    
}
