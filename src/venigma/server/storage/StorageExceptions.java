/*
 * Copyright Evan Summers
 * 
 */
package venigma.server.storage;

import vellum.util.Lists;

/**
 *
 * @author evan
 */
public class StorageExceptions {
    public static String formatMessage(StorageExceptionType storageExceptionType) {
        return storageExceptionType.name();
    }

    public static String formatMessage(StorageExceptionType storageExceptionType, Object[] args) {
        return storageExceptionType.name() + " (" + Lists.formatDisplayComma(args) + ")";
    }
    
    public static StorageRuntimeException newRuntimeException(StorageExceptionType storageExceptionType) {
        return new StorageRuntimeException(storageExceptionType);
    }
    
    public static StorageException newException(StorageExceptionType storageExceptionType) {
        return new StorageException(storageExceptionType);
    }
    
}
