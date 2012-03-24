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

    public VCipherRequest(VCipherRequestType requestType, byte[] bytes) {
        this.requestType = requestType;
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return bytes;
    }
    
    @Override
    public String toString() {
        return requestType.name();
    }               
}
