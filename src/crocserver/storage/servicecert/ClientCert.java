/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.servicecert;

import java.security.cert.X509Certificate;
import java.util.Date;
import vellum.entity.AbstractIdEntity;
import vellum.security.KeyStores;

/**
 *
 * @author evan
 */
public final class ClientCert extends AbstractIdEntity {
    long orgId;
    Long id;
    String hostName;
    String clientName;
    String cert;
    String dname;
    boolean enabled = true;
    Date inserted = new Date();
    Date updated = new Date();
    String updatedBy;
    
    transient String orgName;
    
    public ClientCert() {
    }

    public ClientCert(String updatedBy, long orgId, String hostName, String clientName) {
        this.updatedBy = updatedBy;
        this.orgId = orgId;
        this.hostName = hostName;
        this.clientName = clientName;
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

    public void setX509Cert(X509Certificate x509Cert) {
        this.cert = KeyStores.buildCertPem(x509Cert);
        this.dname = x509Cert.getSubjectDN().getName();
    }
    
    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDname() {
        return dname;
    }
    
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
       
    @Override
    public String toString() {
        return getId().toString();
    }    
}
