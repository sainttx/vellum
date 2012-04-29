/*
 * Copyright Evan Summers
 * 
 */
package venigma.server.storage;

/**
 *
 * @author evan
 */
public enum StorageExceptionType {
    ID_NULL,
    ENTITY_NULL,
    ID_NOT_FOUND,
    ID_ALREADY_EXISTS,
    PAIR_NOT_FOUND,
    PAIR_ALREADY_EXISTS;
    
    public StorageRuntimeException newRuntimeException() {
        return new StorageRuntimeException(this);
    }
    
    public StorageException newException() {
        return new StorageException(this);
    }
    
}
