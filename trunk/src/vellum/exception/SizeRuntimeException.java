/*
 * Copyright Evan Summers
 * 
 */
package vellum.exception;

/**
 *
 * @author evan
 */
public class SizeRuntimeException extends RuntimeException {
    long size;
    
    public SizeRuntimeException(long size) {
        this.size = size;
    }
    
    
}
