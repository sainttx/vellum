/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 * 
 */
package mobi.exception;

/**
 *
 * @author evan.summers
 */
public class MobiException extends RuntimeException {
    MobiExceptionType exceptionType;

    public MobiException(MobiExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public MobiExceptionType getExceptionType() {
        return exceptionType;
    }

    @Override
    public String getMessage() {
        return Resources.getString(getClass(), exceptionType.name());
    }
        
}
