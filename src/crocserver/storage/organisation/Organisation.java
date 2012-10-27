/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.organisation;

import java.util.Date;
import vellum.entity.AbstractIdEntity;

/**
 *
 * @author evan
 */
public class Organisation extends AbstractIdEntity {
    String name;
    String displayName;
    boolean enabled;
    String createdBy;
    Date created;
    Date updated;
    
    public Organisation() {
    }

    public Organisation(String name, String createdBy, boolean enabled) {
        this.name = name;
        this.createdBy = createdBy;
        this.enabled = enabled;
    }
      
    @Override
    public Comparable getId() {
        return name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
       
    @Override
    public String toString() {
        return getId().toString();
    }

    
}
