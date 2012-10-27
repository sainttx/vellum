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
    String publicKey;
    boolean enabled = true;
    Date created = new Date();
    Date updated = new Date();
    
    public ServiceKey() {
    }

    public ServiceKey(String hostName, String serviceName, String adminUserName, String publicKey) {
        this.hostName = hostName;
        this.serviceName = serviceName;
        this.adminUserName = adminUserName;
        this.publicKey = publicKey;
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
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
