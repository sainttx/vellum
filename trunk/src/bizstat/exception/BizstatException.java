/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
 * 
 */
package bizstat.exception;

import vellum.util.Args;

/**
 *
 * @author evan.summers
 */
public class BizstatException extends RuntimeException {

    BizstatExceptionType exceptionType;

    public BizstatException(BizstatExceptionType exceptionType, Comparable id) {
        super(Args.format(exceptionType, id));
        this.exceptionType = exceptionType;
    }

    public BizstatExceptionType getExceptionType() {
        return exceptionType;
    }
        
}

