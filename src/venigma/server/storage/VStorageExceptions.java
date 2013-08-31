/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package venigma.server.storage;

import vellum.util.Args;

/**
 *
 * @author evan
 */
public class VStorageExceptions {
    public static String formatMessage(VStorageExceptionType storageExceptionType) {
        return storageExceptionType.name();
    }

    public static String formatMessage(VStorageExceptionType storageExceptionType, Object[] args) {
        return storageExceptionType.name() + " (" + Args.format(args) + ")";
    }

    public static String formatMessage(Throwable exception, VStorageExceptionType storageExceptionType) {
        return storageExceptionType.name() + " (" + exception.getMessage() + ")";
    }
    
    public static VStorageRuntimeException newRuntimeException(VStorageExceptionType storageExceptionType) {
        return new VStorageRuntimeException(storageExceptionType);
    }
    
    public static VStorageException newException(VStorageExceptionType storageExceptionType) {
        return new VStorageException(storageExceptionType);
    }
    
}
