/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
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
