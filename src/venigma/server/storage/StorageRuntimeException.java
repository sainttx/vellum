/*
 * Copyright Evan Summers
 * 
 */
package venigma.server.storage;

/**
 *
 * @author evan
 */
public class StorageRuntimeException extends RuntimeException {
    StorageExceptionType storageExceptionType;
    
    public StorageRuntimeException(StorageExceptionType storageExceptionType) {
        super(StorageExceptions.formatMessage(storageExceptionType));
        this.storageExceptionType = storageExceptionType;        
    }

    public StorageRuntimeException(StorageExceptionType storageExceptionType, Object ... args) {
        super(StorageExceptions.formatMessage(storageExceptionType, args));
        this.storageExceptionType = storageExceptionType;
        
    }
    
    public StorageExceptionType getStorageExceptionType() {
        return storageExceptionType;
    }
        
}
