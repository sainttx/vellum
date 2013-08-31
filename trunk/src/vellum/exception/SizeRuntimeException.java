/*
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
 * 
 */
package vellum.exception;

/**
 *
 * @author evan.summers
 */
public class SizeRuntimeException extends RuntimeException {
    long size;
    
    public SizeRuntimeException(long size) {
        this.size = size;
    }
    
    
}
