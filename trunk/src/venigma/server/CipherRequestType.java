/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
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
