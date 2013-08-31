/*
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
 * 
 */
package vellumdemo.enigmademo;

/**
 *
 * @author evan.summers
 */
public class EnigmaRequest {
    String message;

    public EnigmaRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }  
}
