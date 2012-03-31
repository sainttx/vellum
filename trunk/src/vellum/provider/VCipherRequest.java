/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

/**
 *
 * @author evan
 */
public class VCipherRequest {
    VCipherRequestType requestType;
    byte[] bytes;
    byte[] iv;    

    public VCipherRequest(VCipherRequestType requestType) {
        this.requestType = requestType;
    }
    
    public VCipherRequest(VCipherRequestType requestType, byte[] bytes) {
        this.requestType = requestType;
        this.bytes = bytes;
    }

    public VCipherRequest(VCipherRequestType requestType, byte[] bytes, byte[] iv) {
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
