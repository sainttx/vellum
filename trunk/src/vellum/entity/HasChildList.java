/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.entity;

import java.util.List;

/**
 *
 * @author evan
 */
public interface HasChildList<T> {
    public List<T> getChildList();
    
}
