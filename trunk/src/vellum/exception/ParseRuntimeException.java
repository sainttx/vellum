/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
 * 
 */
package vellum.exception;

/**
 *
 * @author evan.summers
 */
public class ParseRuntimeException extends RuntimeException {

    public ParseRuntimeException(String message) {
        super(message);
    }

    public ParseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
}
