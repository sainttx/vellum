/*
 * Licensed to the Apache Software Foundation (ASF) by Evan Summers
 * 
 */
package vellum.exception;

/**
 *
 * @author evan.summers
 */
public class EnumException extends DisplayException {
    Enum exceptionType;

    public EnumException(Enum exceptionType, Throwable exception) {
        super(exceptionType.toString(), exception);
        this.exceptionType = exceptionType;
    }
    
    public EnumException(Enum exceptionType) {
        super(exceptionType.toString());
        this.exceptionType = exceptionType;
    }

    public EnumException(Enum exceptionType, Object ... args) {
        super(EnumExceptions.formatMessage(exceptionType, args));
        this.exceptionType = exceptionType;
    }
    
    public Enum getExceptionType() {
        return exceptionType;
    }
        
}
