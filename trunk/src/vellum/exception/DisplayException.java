/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 * 
 */
package vellum.exception;

/**
 *
 * @author evan.summers
 */
public class DisplayException extends Exception implements DisplayMessage {
    String displayMessage;
    
    public DisplayException(String displayMessage, Throwable exception) {
        super(displayMessage, exception);
        this.displayMessage = displayMessage;
    }

    public DisplayException(String displayMessage) {
        super(displayMessage);
        this.displayMessage = displayMessage;
    }
    
    @Override
    public String getDisplayMessage() {
        return displayMessage;
    }
}
