/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
 * 
 */
package vellum.validation;

import vellum.exception.*;

/**
 *
 * @author evan.summers
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
