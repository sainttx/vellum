/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

/**
 *
 * @author evan
 */
public class VCipherResponse {
    VCipherResponseType responseType;
    byte[] bytes;
    byte[] iv;

    public VCipherResponse(VCipherResponseType responseType, byte[] bytes) {
        this.responseType = responseType;
        this.bytes = bytes;
    }

    public VCipherResponse(VCipherResponseType responseType) {
        this.responseType = responseType;
    }
    
    public byte[] getBytes() {
        return bytes;
    }

    public byte[] getIv() {
        return iv;
    }
    
    public VCipherResponseType getResponseType() {
        return responseType;
    }
    
    @Override
    public String toString() {
        return responseType.name();
    }   
}
