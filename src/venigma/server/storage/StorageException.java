/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package venigma.server.storage;

/**
 *
 * @author evan
 */
public class StorageException extends Exception {
    StorageExceptionType storageExceptionType;
    
    public StorageException(StorageExceptionType storageExceptionType) {
        this.storageExceptionType = storageExceptionType;
    }

    public StorageExceptionType getStorageExceptionType() {
        return storageExceptionType;
    }
    
}
