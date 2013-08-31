/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
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
