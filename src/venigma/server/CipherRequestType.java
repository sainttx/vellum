/*
 * Copyright Evan Summers
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
    START_SAFE_MODE,
    LIST_EMPTY_PASSWORDS,
    SET_PASSWORD,
    START,
    STOP,
    ADD_KEY,
    GENERATE_KEY,
    REVISE_KEY,
    ADD_USER,
    REGISTER_USER,
    CONFIRM_USER,
    CONFIRM_KEY,
    GRANT,
    REVOKE,
    ENCIPHER,
    DECIPHER,
    RECIPHER,
    
}
