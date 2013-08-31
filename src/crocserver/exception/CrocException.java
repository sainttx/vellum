/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package crocserver.exception;

import vellum.exception.EnumException;

/**
 *
 * @author evan.summers
 */
public class CrocException extends EnumException {
    
    public CrocException(CrocExceptionType exceptionType) {
        super(exceptionType);
    }
    
    public CrocException(CrocExceptionType exceptionType, Object ... args) {
        super(exceptionType, args);
    }   
}
