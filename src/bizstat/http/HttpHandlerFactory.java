/*
 * Licensed to the Apache Software Foundation (ASF) by Evan Summers
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
