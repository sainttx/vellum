/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package venigma.server;

/**
 *
 * @author evan
 */
public enum CipherErrorType {
    CERT,
    SUBJECT,
    AUTH,
    NOT_PROVIDER,
    NOT_USER,
    NOT_ADMIN,
    NOT_STARTED,
    USER_NOT_FOUND;
    
}
