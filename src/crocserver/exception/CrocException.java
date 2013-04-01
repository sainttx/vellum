/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.exception;

import vellum.exception.EnumException;

/**
 *
 * @author evan
 */
public class CrocException extends EnumException {
    
    public CrocException(CrocExceptionType exceptionType) {
        super(exceptionType);
    }
    
    public CrocException(CrocExceptionType exceptionType, Object ... args) {
        super(exceptionType, args);
    }   
}
