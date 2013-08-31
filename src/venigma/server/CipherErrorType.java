/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
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
