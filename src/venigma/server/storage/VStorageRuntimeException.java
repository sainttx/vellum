/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
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
