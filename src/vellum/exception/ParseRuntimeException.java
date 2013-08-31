/*
 * Licensed to the Apache Software Foundation (ASF) by Evan Summers
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
