/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
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
