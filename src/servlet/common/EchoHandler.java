/*
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 */
package servlet.common;

import com.sun.net.httpserver.Headers;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author evanx
 */
public class EchoHandler implements HttpHandler {

    public void handle(HttpExchange httpExchange) throws IOException {
        StringBuilder response = new StringBuilder();
        Headers headers = httpExchange.getRequestHeaders();
        Set<Map.Entry<String, List<String>>> entries = headers.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            response.append(entry.toString());
            response.append("\n");
        }
        response.append("hello, ");
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
}