/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package vellum.entity;

/**
 *
 * @author evan.summers
 */
public abstract class AbstractLongIdEntity extends AbstractIdEntity {
    protected Long id;
    
    @Override
    public Comparable getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
       
}
