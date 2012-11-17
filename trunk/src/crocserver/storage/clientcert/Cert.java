/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.clientcert;

import java.security.cert.X509Certificate;
import java.util.Date;
import vellum.entity.AbstractIdEntity;
import vellum.security.KeyStores;

/**
 *
 * @author evan
 */
public final class Cert extends AbstractIdEntity {
    Long id;
    String subject;
    String cert;
    String ipAddress;
    boolean enabled = true;
    Date inserted = new Date();
    Date updated = new Date();
    String updatedBy;
    boolean stored = false;
    
    public Cert() {
    }

    public void setCert(X509Certificate x509Cert) {
        this.cert = KeyStores.buildCertPem(x509Cert);
        this.subject = x509Cert.getSubjectDN().getName();
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
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

    public void setStored(boolean stored) {
        this.stored = stored;
    }

    public boolean isStored() {
        return stored;
    }
        
    @Override
    public String toString() {
        return getId().toString();
    }    
}
