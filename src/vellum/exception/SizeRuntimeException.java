/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
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
