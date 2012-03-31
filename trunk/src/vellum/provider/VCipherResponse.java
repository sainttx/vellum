/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

import vellum.util.Args;
import vellum.util.Bytes;

/**
 *
 * @author evan
 */
public class VCipherResponse {
    VCipherResponseType responseType;
    String errorMessage;
    byte[] bytes;
    byte[] iv;

    public VCipherResponse(VCipherResponseType responseType, byte[] bytes) {
        this.responseType = responseType;
        this.bytes = bytes;
    }

    public VCipherResponse(VCipherResponseType responseType, byte[] bytes, byte[] iv) {
        this.responseType = responseType;
        this.bytes = bytes;
        this.iv = iv;
    }
    
    public VCipherResponse(VCipherResponseType responseType) {
        this.responseType = responseType;
    }

    public VCipherResponse(Throwable throwable) {
        this.responseType = VCipherResponseType.ERROR;
        this.errorMessage = throwable.getMessage();
    }
    
    public VCipherResponse(String errorMessage) {
        this.responseType = VCipherResponseType.ERROR;
        this.errorMessage = errorMessage;
    }
    
    public byte[] getBytes() {
        return bytes;
    }

    public byte[] getIv() {
        return iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }
        
    public VCipherResponseType getResponseType() {
        return responseType;
    }
    
    @Override
    public String toString() {
        return Args.formatPrint(responseType.name(), errorMessage, Bytes.formatHex(bytes), Bytes.formatHex(iv));
    }   
}
