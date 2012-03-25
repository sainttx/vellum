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

    public VCipherRequest(VCipherRequestType requestType, byte[] bytes) {
        this.requestType = requestType;
        this.bytes = bytes;
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
