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
    CONNECTION_ERROR,
    ID_NULL,
    ENTITY_NULL,
    KEY_NULL,
    KEY_NOT_FOUND,
    KEY_NOT_INSERTED,
    KEY_NOT_UPDATED,
    KEY_NOT_DELETED,
    KEY_ALREADY_EXISTS,
    KEY_NOT_DECRYPTED,
    ID_NOT_FOUND,
    ID_ALREADY_EXISTS,
    PAIR_NOT_FOUND,
    PAIR_ALREADY_EXISTS,
    NO_DATABASE_STORE_PASSWORD,
    NO_DATABASE_NAME;
    
    public StorageRuntimeException newRuntimeException() {
        return new StorageRuntimeException(this);
    }
    
    public StorageException newException() {
        return new StorageException(this);
    }
    
}
