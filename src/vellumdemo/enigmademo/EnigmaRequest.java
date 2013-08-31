/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
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
