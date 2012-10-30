/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.org;

import java.util.Date;
import vellum.entity.AbstractIdEntity;

/**
 *
 * @author evan
 */
public class Org extends AbstractIdEntity {
    Long id;
    String name;
    String displayName;
    String url;
    String org;
    String region;
    String locality;
    String country;
    boolean enabled = true;
    String updatedBy;
    Date inserted;
    Date updated;
    
    public Org() {
    }

    public Org(String name, String updatedBy) {
        this.name = name;
        this.updatedBy = updatedBy;
    }
      
    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getInserted() {
        return inserted;
    }

    public void setInserted(Date inserted) {
        this.inserted = inserted;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }
    
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
