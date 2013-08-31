/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
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
