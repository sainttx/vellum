/*
 * Copyright Evan Summers
 * 
 */
package crocserver.app;

/**
 *
 * @author evan
 */
public enum CrocExceptionType {
    NO_COOKIE,
    EXPIRED_COOKIE,
    STALE_COOKIE,
    INVALID_COOKIE,
    NO_AUTH,
    AUTH_FAILED,
    AUTH_EXPIRED,
}
