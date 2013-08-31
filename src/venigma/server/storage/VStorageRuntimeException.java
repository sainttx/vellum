/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 * 
 */
package venigma.server.storage;

/**
 *
 * @author evan
 */
public class VStorageRuntimeException extends RuntimeException {
    VStorageExceptionType storageExceptionType;

    public VStorageRuntimeException(VStorageExceptionType storageExceptionType, Throwable exception) {
        super(VStorageExceptions.formatMessage(exception, storageExceptionType), exception);
        this.storageExceptionType = storageExceptionType;
    }
    
    public VStorageRuntimeException(VStorageExceptionType storageExceptionType) {
        super(VStorageExceptions.formatMessage(storageExceptionType));
        this.storageExceptionType = storageExceptionType;
    }

    public VStorageRuntimeException(VStorageExceptionType storageExceptionType, Object ... args) {
        super(VStorageExceptions.formatMessage(storageExceptionType, args));
        this.storageExceptionType = storageExceptionType;
    }
    
    public VStorageExceptionType getStorageExceptionType() {
        return storageExceptionType;
    }
        
}
