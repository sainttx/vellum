/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012, Evan Summers
 * 
 */
package vellum.storage;

import vellum.util.Args;

/**
 *
 * @author evan
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
