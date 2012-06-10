/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package venigma.server;

/**
 *
 * @author evan
 */
public enum CipherRequestType {
    PING,
    CHECK,
    ADD_USER,
    REGISTER_USER,
    REGISTER_CIPHER,
    CONFIRM_USER,
    LIST_EMPTY_PASSWORDS,
    SET_PASSWORD,
    ADD_KEY,
    GENERATE_KEY,
    REVISE_KEY,
    CONFIRM_KEY,
    START_SAFE_MODE,
    START,
    STOP,
    GRANT,
    REVOKE,
    ENCIPHER,
    DECIPHER,
    RECIPHER,
    
}
