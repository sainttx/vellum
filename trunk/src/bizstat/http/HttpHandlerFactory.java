/*
 * Copyright Evan Summers
 * 
 */
package bizstat.http;

import com.sun.net.httpserver.HttpHandler;

/**
 *
 * @author evan
 */
public interface HttpHandlerFactory {
    public HttpHandler createHttpHandler(String context);
}
