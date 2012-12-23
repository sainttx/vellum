/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package vellum.validation;

import vellum.exception.*;

/**
 *
 * @author evan
 */
public class ValidationException extends EnumException {
    String parameterName;

    public ValidationException(ValidationExceptionType exceptionType, String parameterName) {
        super(exceptionType);
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }            
}
