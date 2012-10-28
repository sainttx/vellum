/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.servicekey;

import java.util.Date;
import vellum.entity.AbstractIdEntity;

/**
 *
 * @author evan
 */
public class ServiceCert extends AbstractIdEntity {
    long orgId;
    Long id;
    String hostName;
    String serviceName;
    String cert;
    boolean enabled = true;
    Date inserted = new Date();
    Date updated = new Date();
    
    transient String orgName;
    
    public ServiceCert() {
    }

    public ServiceCert(long orgId, String hostName, String serviceName, String cert) {
        this.orgId = orgId;
        this.hostName = hostName;
        this.serviceName = serviceName;
        this.cert = cert;
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }
        
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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
