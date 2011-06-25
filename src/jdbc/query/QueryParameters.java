/*
 * Copyright 2011, iPay (Pty) Ltd, Evan Summers
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 */

package jdbc.query;

import bizmon.parameter.Parameters;
import bizmon.util.Args;
import java.util.Map;

/**
 *
 * @author evanx
 */
public class QueryParameters {
    String database;
    String schema;
    String query;
            
    public QueryParameters() {
    }

    public QueryParameters(String database, String schema, String query) {
        this.database = database;
        this.schema = schema;
        this.query = query;
    }

    public void init(String[] args) {
        init(Parameters.createMap(args));
    }

    public void init(Map<String, String> map) {
        database = map.get("database");
        schema = map.get("schema");
        query = map.get("query");
    }

    @Override
    public String toString() {
        return Args.formatPrint(database, schema, query);
    }
            
}
