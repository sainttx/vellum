/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
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
