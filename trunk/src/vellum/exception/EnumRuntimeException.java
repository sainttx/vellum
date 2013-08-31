/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
 * 
 */
package vellum.exception;

/**
 *
 * @author evan.summers
 */
public class EnumRuntimeException extends RuntimeException {
    Enum exceptionType;

    public EnumRuntimeException(Enum exceptionType, Throwable exception) {
        super(EnumExceptions.formatMessage(exception, exceptionType), exception);
        this.exceptionType = exceptionType;
    }
    
    public EnumRuntimeException(Enum exceptionType) {
        super(EnumExceptions.formatMessage(exceptionType));
        this.exceptionType = exceptionType;
    }

    public EnumRuntimeException(Enum exceptionType, Object ... args) {
        super(EnumExceptions.formatMessage(exceptionType, args));
        this.exceptionType = exceptionType;
    }
    
    public Enum getStorageExceptionType() {
        return exceptionType;
    }
        
}
