/*
 * (c) Copyright 2011, iPay (Pty) Ltd, Evan Summers
 * Apache Software License 2.0
 * Supported by BizSwitch.net
 */

package bizdb.meta;

import bizmon.parameter.Parameters;
import bizmon.util.Args;

/**
 *
 * @author evanx
 */
public class DatabaseParameters {
    String database;
    String schema;
            
    public DatabaseParameters() {
    }
    
    public void init(String[] args) {
        database = Parameters.getString("database", args, 0);
        schema = Parameters.getString("schema", args, 1);
    }

    @Override
    public String toString() {
        return Args.formatPrint(database, schema);
    }
            
}
