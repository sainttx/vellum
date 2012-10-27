/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.adminuser;

import java.util.Date;
import vellum.entity.AbstractIdEntity;

/**
 *
 * @author evan
 */
public class AdminUser extends AbstractIdEntity {
    String username;
    String displayName;
    AdminRole role;
    boolean enabled;
    String email;
    Date created;
    Date updated;
    String createdBy;
    String secondedBy;
    String passwordHash;
    String passwordSalt;
    Date lastLogin;
    String publicKey;
    
    public AdminUser() {
    }

    public AdminUser(String username, boolean enabled) {
        this.username = username;
        this.enabled = enabled;
    }
    
    public AdminUser(String username, String displayName, AdminRole role, boolean enabled) {
        this.username = username;
        this.displayName = displayName;
        this.role = role;
        this.enabled = enabled;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
    
    @Override
    public Comparable getId() {
        return username;
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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
