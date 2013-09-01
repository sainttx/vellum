/*
       Source https://code.google.com/p/vellum by @evanxsummers
 * Apache Software License 2.0
 * Supported by iPay (Pty) Ltd, BizSwitch.net
 */

package vellum.query;

import vellum.query.QueryInfo;
import vellum.query.QueryInfoMap;
import java.io.InputStream;

/**
 *
 * @author evan.summers
 */
public enum QueryResource {
    common,
    terminal,
    month,
    incoming;

    QueryInfoMap map;

    public InputStream getStream() {
        return getClass().getResourceAsStream(name() + ".sql");
    }

    public QueryInfoMap getMap() {
        if (map == null) {
            map = new QueryInfoMap(this);
        }
        return map;
    }

    public QueryInfo get(String queryName) {
        return getMap().get(queryName);
    }

}
