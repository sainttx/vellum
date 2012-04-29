/*
 * Copyright Evan Summers
 * 
 */
package venigma.server.storage;

/**
 *
 * @author evan
 */
public class StorageExceptions {
    public static String formatMessage(StorageExceptionType storageExceptionType) {
        return storageExceptionType.name();
    }
    
    public static StorageRuntimeException newRuntimeException(StorageExceptionType storageExceptionType) {
        return new StorageRuntimeException(storageExceptionType);
    }
    
    public static StorageException newException(StorageExceptionType storageExceptionType) {
        return new StorageException(storageExceptionType);
    }
    
}
