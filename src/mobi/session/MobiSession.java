/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
 * 
 */
package mobi.session;

/**
 *
 * @author evan.summers
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
