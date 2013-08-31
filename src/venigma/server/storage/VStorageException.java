/*
 * Licensed to the Apache Software Foundation (ASF) by Evan Summers
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
