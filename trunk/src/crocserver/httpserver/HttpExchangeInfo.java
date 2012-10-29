/*
 * Copyright Evan Summers
 * 
 */
package crocserver.httpserver;

import com.sun.net.httpserver.HttpExchange;
import java.beans.PropertyDescriptor;
import java.util.List;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.parameter.Entry;
import vellum.parameter.ParameterMap;
import vellum.parameter.Parameters;
import vellum.util.Beans;
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
    String[] args;
    boolean headersParsed = false;
    boolean acceptGzip = false;
    boolean agentWget = false;

    public HttpExchangeInfo(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    public String getPath() {
        return httpExchange.getRequestURI().getPath();
    }
    
    public String[] getPathArgs() {
        if (args == null) {
            args = httpExchange.getRequestURI().getPath().substring(1).split("/");
        }
        return args;
    }

    public int getPathLength() {
        return getPathArgs().length;
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

    public String getParameter(String key) {
        return parameterMap.get(key);
    }
    
    private void put(String string) {
        Entry<String, String> entry = Parameters.parseEntry(string);
        if (entry != null) {
            String value = Strings.decodeUrl(entry.getValue());
            parameterMap.put(entry.getKey(), value);
        }
    }

    public void parseHeaders() {
        headersParsed = true;
        for (String key : httpExchange.getRequestHeaders().keySet()) {
            List<String> values = httpExchange.getRequestHeaders().get(key);
            logger.verbose("parseHeaders", key, values);
            if (key.equals("Accept-encoding")) {
                if (values.contains("gzip")) {
                    acceptGzip = true;
                }
            } else if (key.equals("User-agent")) {
                for (String value : values) {
                    if (value.toLowerCase().contains("wget")) {
                        agentWget = true;
                    }
                }
            }
        }
    }

    public void setBean(Object bean) {
        for (PropertyDescriptor property : Beans.getPropertyMap(bean.getClass()).values()) {
            String stringValue = parameterMap.get(property.getName());
            if (stringValue != null) {
                Beans.parse(bean, property, stringValue);
            }
        }
    }

    public boolean isAgentWget() {
        if (!headersParsed) {
            parseHeaders();
        }
        return agentWget;
    }

    public boolean isAcceptGzip() {
        if (!headersParsed) {
            parseHeaders();
        }
        return acceptGzip;
    }

    public String getPathString(int index) {
        return getPathString(index, null);
    }
    
    public String getPathString(int index, String defaultValue) {
        String[] args = getPathArgs();
        if (args.length > index) {
            return args[index];
        }
        return defaultValue;
    }
    
}
