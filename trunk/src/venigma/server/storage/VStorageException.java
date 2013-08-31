/*
 *    https://code.google.com/p/vellum - Contributed by Evan Summers
 * 
 */
package venigma.server.storage;

/**
 *
 * @author evan
 */
public class VStorageException extends Exception {
    VStorageExceptionType storageExceptionType;
    
    public VStorageException(VStorageExceptionType storageExceptionType) {
        this.storageExceptionType = storageExceptionType;
    }

    public VStorageExceptionType getStorageExceptionType() {
        return storageExceptionType;
    }
    
}
