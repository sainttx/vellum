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

    public VCipherRequest(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return requestType.name();
    }               
}
