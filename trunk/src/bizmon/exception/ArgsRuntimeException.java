/*
 * Apache Software License 2.0
 * Supported by BizSwitch.net   
 */
package bizmon.exception;

/**
 * Utility methods related to using loggers.
 *
 * @author evan
 */
public class ArgsRuntimeException extends RuntimeException {
    Object[] args;
    
    public ArgsRuntimeException(Object ... args) {
        super(Exceptions.getMessage(args), Exceptions.getThrowable(args));
        this.args = args;        
    }

}
