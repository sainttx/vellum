/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
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
