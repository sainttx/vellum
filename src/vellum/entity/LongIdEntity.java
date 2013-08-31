/*
 * Licensed to the Apache Software Foundation (ASF) by Evan Summers
 * 
 */
package vellum.entity;

/**
 *
 * @author evan.summers
 */
public interface LongIdEntity extends IdEntity<Long> {
    public void setId(Long id);
       
}
