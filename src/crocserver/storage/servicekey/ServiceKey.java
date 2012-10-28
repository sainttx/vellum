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
public class ServiceKey extends AbstractIdEntity {
    Long id;
    String hostName;
    String serviceName;
    String adminUserName;
    String cert;
    boolean enabled = true;
    Date inserted = new Date();
    Date updated = new Date();
    
    public ServiceKey() {
    }

    public ServiceKey(String adminUserName, String hostName, String serviceName, String publicKey) {
        this.adminUserName = adminUserName;
        this.hostName = hostName;
        this.serviceName = serviceName;
        this.cert = publicKey;
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getAdminUserName() {
        return adminUserName;
    }

    public void setAdminUserName(String adminUserName) {
        this.adminUserName = adminUserName;
    }

    public Date getInserted() {
        return inserted;
    }

    public void setInserted(Date inserted) {
        this.inserted = inserted;
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
