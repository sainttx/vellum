/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
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
