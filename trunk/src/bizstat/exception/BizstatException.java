/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
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

