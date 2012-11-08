/*
 * Copyright Evan Summers
 * 
 */
package mobi.session;

/**
 *
 * @author evan
 */
public class MobiSession {
      
    String email;
    long lastRequestMillis;

    MobiSession() {
    }

    public MobiSession(String email) {
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getLastRequestMillis() {
        return lastRequestMillis;
    }

    public void setLastRequestMillis(long lastRequestMillis) {
        this.lastRequestMillis = lastRequestMillis;
    }
   
}
