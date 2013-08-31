/*
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
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
