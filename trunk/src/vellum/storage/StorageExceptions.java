/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package vellum.storage;

import vellum.util.Args;

/**
 *
 * @author evan.summers
 */
public class StorageExceptions {
    public static String formatMessage(StorageExceptionType storageExceptionType) {
        return storageExceptionType.name();
    }

    public static String formatMessage(StorageExceptionType storageExceptionType, Object[] args) {
        return storageExceptionType.name() + " (" + Args.format(args) + ")";
    }

    public static String formatMessage(Throwable exception, StorageExceptionType storageExceptionType) {
        return storageExceptionType.name() + " (" + exception.getMessage() + ")";
    }
    
    public static StorageRuntimeException newRuntimeException(StorageExceptionType storageExceptionType) {
        return new StorageRuntimeException(storageExceptionType);
    }
    
    public static StorageException newException(StorageExceptionType storageExceptionType) {
        return new StorageException(storageExceptionType);
    }
    
}
