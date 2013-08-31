/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package crocserver.app;

import vellum.exception.DisplayMessage;

/**
 *
 * @author evan.summers
 */
public enum CrocExceptionType implements DisplayMessage {
    NO_COOKIE,
    EXPIRED_COOKIE,
    STALE_COOKIE,
    INVALID_COOKIE,
    NO_AUTH,
    AUTH_FAILED,
    AUTH_EXPIRED,
    PASSWORD_TOO_SHORT,;

    @Override
    public String getDisplayMessage() {
        return name();
    }
}
