/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
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

