/*
 * Copyright Evan Summers
 * 
 */
package venigmon.httpserver;

import com.sun.net.httpserver.HttpExchange;
import java.util.List;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.parameter.Entry;
import vellum.parameter.ParameterMap;
import vellum.parameter.Parameters;
import vellum.util.Strings;

/**
 *
 * @author evan
 */
public class HttpExchangeInfo {

    Logr logger = LogrFactory.getLogger(getClass());
    HttpExchange httpExchange;
    ParameterMap parameterMap;
    String urlQuery;

    public HttpExchangeInfo(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    public String getPath() {
        return httpExchange.getRequestURI().getPath();
    }
    
    public String[] splitPath() {
        return httpExchange.getRequestURI().getPath().substring(1).split("/");
    }

    public ParameterMap getParameterMap() {
        if (parameterMap == null) {
            parseParameterMap();
        }
        return parameterMap;
    }

    private void parseParameterMap() {
        parameterMap = new ParameterMap();
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

    private void put(String string) {
        Entry<String, String> entry = Parameters.parseEntry(string);
        if (entry != null) {
            String value = Strings.decodeUrl(entry.getValue());
            parameterMap.put(entry.getKey(), value);
        }
    }

    public void parseHeaders() {
        for (String key : httpExchange.getRequestHeaders().keySet()) {
            List<String> values = httpExchange.getRequestHeaders().get(key);
            logger.info("parseHeaders", key, values);
        }
        logger.verbose("parseHeaders");
    }

}
