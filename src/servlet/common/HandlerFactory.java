/*
 * Copyright 2011, Evan Summers
 * Apache Software License 2.0
 */
package servlet.common;

/**
 *
 * @author evan
 */
public interface HandlerFactory {
    public HttpHandler newHandler() throws Exception;
    
}
