/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.exception;

/**
 *
 * @author evan
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
