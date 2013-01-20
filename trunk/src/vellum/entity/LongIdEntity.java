/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.entity;

/**
 *
 * @author evan
 */
public interface LongIdEntity extends IdEntity<Long> {
    public void setId(Long id);
       
}
