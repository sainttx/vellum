/*
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
 * 
 */
package venigma.server;

/**
 *
 * @author evan
 */
public class CipherResponseRuntimeException extends RuntimeException {
 
    CipherResponse response;

    public CipherResponseRuntimeException(CipherResponse response) {
        super(getMessage(response));
        this.response = response;
    }

    public CipherResponse getResponse() {
        return response;
    }
        
    public static String getMessage(CipherResponse response) {
        return response.getResponseType().name();
    }
    
}
