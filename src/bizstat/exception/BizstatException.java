/*
 * Apache Software License 2.0, Apache Software License 2.0, (c) Copyright 2012, Evan Summers 2012, Evan Summers
 * 
 */
package bizstat.exception;

import vellum.util.Args;

/**
 *
 * @author evan
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

