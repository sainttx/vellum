/*
 * Copyright Evan Summers
 * 
 */
package crocserver.app;

/**
 *
 * @author evan
 */
public class CrocUserSession {

    String email;
    long authMillis;

    public CrocUserSession(String email, long authMillis) {
        this.email = email;
        this.authMillis = authMillis;
    }

    public String getEmail() {
        return email;
    }

    public long getAuthMillis() {
        return authMillis;
    }   
}
