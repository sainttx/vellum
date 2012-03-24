/*
 * Copyright Evan Summers
 * 
 */
package vellum.provider;

/**
 *
 * @author evan
 */
public class VCipherResponseRuntimeException extends RuntimeException {
 
    VCipherResponse response;

    public VCipherResponseRuntimeException(VCipherResponse response) {
        super(getMessage(response));
        this.response = response;
    }

    public VCipherResponse getResponse() {
        return response;
    }
        
    public static String getMessage(VCipherResponse response) {
        return response.getResponseType().name();
    }
    
}
