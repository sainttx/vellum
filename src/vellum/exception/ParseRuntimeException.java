/*
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
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
