/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package common.entity;

import common.entity.Entities;
import common.entity.Entity;

/**
 *
 * @author evan
 */
public abstract class AbstractEntity implements Entity {

    @Override
    public boolean equals(Object obj) {
        return Comparables.equals(getId(), Entities.getId(obj));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    
}
