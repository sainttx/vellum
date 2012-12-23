/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.org;

import java.util.Date;
import vellum.datatype.Patterns;
import vellum.entity.AbstractIdEntity;
import vellum.parameter.StringMap;
import vellum.validation.ValidationException;
import vellum.validation.ValidationExceptionType;

/**
 *
 * @author evan
 */
public final class Org extends AbstractIdEntity {
    Long id;
    String orgName;
    String displayName;
    String url;
    String region;
    String locality;
    String country;
    boolean enabled = true;
    String updatedBy;
    Date inserted;
    Date updated;
    boolean stored = false;
            
    public Org() {
    }

    public Org(String orgName, String updatedBy) {
        this.orgName = orgName;
        this.updatedBy = updatedBy;
    }
      
    public Org(StringMap map) {
        update(map);
    }

    public void update(Org bean) {
        update(bean.getStringMap());
    }
    
    public void update(StringMap map) {
        orgName = map.get("orgName");
        url = map.get("url");
        displayName = map.get("displayName");
        region = map.get("region");
        locality = map.get("locality");
        country = map.get("country");        
    }
    
    public StringMap getStringMap() {
        StringMap map = new StringMap();
        map.put("orgId", id);
        map.put("updatedBy", updatedBy);
        map.put("orgName", orgName);
        map.put("displayName", displayName);
        map.put("url", url);
        map.put("region", region);
        map.put("locality", locality);
        map.put("country", country);
        map.put("enabled", enabled);
        return map;
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
        return orgName;
    }

    public void setName(String name) {
        this.orgName = name;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public void setStored(boolean stored) {
        this.stored = stored;
    }
    
    public boolean isStored() {
        return stored;
    }

    public void validate() throws ValidationException {
        if (!Patterns.matchesUrl(url)) {
            throw new ValidationException(ValidationExceptionType.INVALID_URL, url);
        }
    }
    
    @Override
    public String toString() {
        return getId().toString();
    }
    
}
