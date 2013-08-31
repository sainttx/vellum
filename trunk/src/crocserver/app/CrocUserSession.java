/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package crocserver.app;

/**
 *
 * @author evan.summers
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
