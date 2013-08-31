/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
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
