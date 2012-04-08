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
    ENCIPHER,
    DECIPHER,
    START,
    STOP,
    GRANT,
    REVOKE,
    GENKEY
    
}
