/*
 * Copyright Evan Summers
 * 
 */
package venigma.common;

import java.util.Date;

/**
 *
 * @author evan
 */
public class AdminUser {
    String username;
    String displayName;
    AdminRole role;
    boolean enabled;
    String email;
    Date timeCreated;
    String createdBy;
    String secondedBy;
    
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
   
}
