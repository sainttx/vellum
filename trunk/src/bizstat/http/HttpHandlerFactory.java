/*
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
 * 
 */
package bizstat.http;

import com.sun.net.httpserver.HttpHandler;

/**
 *
 * @author evan.summers
 */
public interface HttpHandlerFactory {
    public HttpHandler createHttpHandler(String context);
}
