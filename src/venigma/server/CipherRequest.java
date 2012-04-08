/*
 * Copyright Evan Summers
 * 
 */
package venigma.server;

import venigma.common.AdminRole;

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
        
    @Override
    public String toString() {
        return requestType.name();
    }               
}
