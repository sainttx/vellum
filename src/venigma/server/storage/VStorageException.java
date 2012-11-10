/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012, Evan Summers
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
