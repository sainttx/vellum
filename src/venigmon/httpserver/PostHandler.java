/*
 * (c) Copyright 2010, iPay (Pty) Ltd
 */
package venigmon.httpserver;

import bizmon.parameter.Entry;
import bizmon.parameter.ParameterMap;
import bizmon.parameter.Parameters;
import bizstat.server.BizstatServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.printer.PrintStreamAdapter;
import vellum.printer.Printer;
import vellum.util.Lists;
import vellum.util.Strings;

/**
 *
 * @author evans
 */
public class PostHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(getClass());

    BizstatServer context;
    HttpExchange httpExchange;
    String urlQuery;
    String path;
    String[] pathArgs;    
    Printer out;
    ParameterMap parameterMap = new ParameterMap();

    public PostHandler(BizstatServer context) {
        super();
        this.context = context;
    }
    
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;
        path = httpExchange.getRequestURI().getPath();
        pathArgs = path.substring(1).split("/");
        logger.info("pathArgs", Lists.format(pathArgs));
        parseParameterMap();
        parseHeaders();
        logger.info("parameterMap", parameterMap);
        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        httpExchange.getResponseHeaders().set("Content-type", "text/plain");
        out = new PrintStreamAdapter(httpExchange.getResponseBody());
        httpExchange.getRequestBody();
        out.println("OK");
        out.close();
        httpExchange.close();
    }
    
    protected void parseParameterMap() {
        urlQuery = httpExchange.getRequestURI().getQuery();
        if (urlQuery == null) {
            return;
        }
        int index = 0;
        while (true) {
            int endIndex = urlQuery.indexOf("&", index);
            if (endIndex > 0) {
                put(urlQuery.substring(index, endIndex));
                index = endIndex + 1;
            } else if (index < urlQuery.length()) {
                put(urlQuery.substring(index));
                return;
            }
        }
    }
    
    protected void put(String string) {
        logger.info(string);
        Entry<String, String> entry = Parameters.parseEntry(string);
        if (entry != null) {
            parameterMap.put(entry.getKey(), Strings.decodeUrl(entry.getValue()));
        }
    }

    protected void parseHeaders() {
        for (String key : httpExchange.getRequestHeaders().keySet()) {
            List<String> values = httpExchange.getRequestHeaders().get(key);
            logger.info("parseHeaders", key, values);
        }
        logger.verbose("parseHeaders");
    }
    
}
