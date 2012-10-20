/*
 * Copyright Evan Summers
 * 
 */
package vellum.exception;

/**
 *
 * @author evan
 */
public class ParseRuntimeException extends RuntimeException {

    public ParseRuntimeException(String message) {
        super(message);
    }

    public ParseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
}
