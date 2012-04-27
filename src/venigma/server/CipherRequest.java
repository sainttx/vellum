/*
 * Copyright Evan Summers
 * 
 */
package venigma.server;

import venigma.common.AdminRole;
import venigma.common.AdminUser;

/**
 *
 * @author evan
 */
public class CipherRequest {
    CipherRequestType requestType;
    byte[] bytes;
    byte[] iv;
    String keyAlias;
    int keyRevision;
    String username;
    AdminRole role;
    AdminUser user;
    
    public CipherRequest(CipherRequestType requestType) {
        this.requestType = requestType;
    }
    
    public CipherRequest(CipherRequestType requestType, byte[] bytes) {
        this.requestType = requestType;
        this.bytes = bytes;
    }

    public CipherRequest(CipherRequestType requestType, byte[] bytes, byte[] iv) {
        this.requestType = requestType;
        this.bytes = bytes;
        this.iv = iv;
    }
    
    public byte[] getBytes() {
        return bytes;
    }

    public byte[] getIv() {
        return iv;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUser(AdminUser user) {
        this.user = user;
    }
    
    public AdminUser getUser() {
        return user;
    }

    public void setRole(AdminRole role) {
        this.role = role;
    }

    public AdminRole getRole() {
        return role;
    }
    
    @Override
    public String toString() {
        return requestType.name();
    }

}
