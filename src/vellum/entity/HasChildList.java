/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
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
