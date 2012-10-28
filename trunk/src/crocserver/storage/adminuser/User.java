/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.adminuser;

import crocserver.storage.org.Org;
import java.util.Date;
import vellum.entity.AbstractIdEntity;

/**
 *
 * @author evan
 */
public class User extends AbstractIdEntity<String> {
    String userName;
    String displayName;
    AdminRole role;
    long orgId;
    Org org;
    boolean enabled = true;
    String email;
    Date inserted;
    Date updated;
    String createdBy;
    String secondedBy;
    String passwordHash;
    String passwordSalt;
    Date lastLogin;
    String publicKey;
    
    public User() {
    }

    public User(Org org, String userName) {
        this.org = org;
        this.userName = userName;
    }
    
    public User(String userName, String displayName, AdminRole role, boolean enabled) {
        this.userName = userName;
        this.displayName = displayName;
        this.role = role;
        this.enabled = enabled;
    }

    @Override
    public String getId() {
        return userName;
    }

    public Org getOrg() {
        return org;
    }
    
    public void setOrg(Org org) {
        this.org = org;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public long getOrgId() {
        return orgId;
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

    public AdminRole getRole() {
        return role;
    }

    public void setRole(AdminRole role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getSecondedBy() {
        return secondedBy;
    }

    public void setSecondedBy(String secondedBy) {
        this.secondedBy = secondedBy;
    }

    public Date getInserted() {
        return inserted;
    }

    public void setInserted(Date inserted) {
        this.inserted = inserted;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getPublicKey() {
        return publicKey;
    }
        
    @Override
    public String toString() {
        return getId().toString();
    }

    
}
