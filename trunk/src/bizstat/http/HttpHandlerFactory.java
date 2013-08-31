/*
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
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
