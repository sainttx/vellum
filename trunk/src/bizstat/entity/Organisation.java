/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package bizstat.entity;

import vellum.entity.StringIdEntity;

/**
 *
 * @author evan
 */
public class Organisation extends StringIdEntity {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
        
}
