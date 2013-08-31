/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
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
