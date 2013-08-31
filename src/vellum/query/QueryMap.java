/*
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 * 
 */
package vellum.query;

import vellum.exception.Exceptions;
import vellum.util.Streams;
import java.io.BufferedReader;
import java.io.InputStream;
import java.util.HashMap;

/**
 *
 * @author evan.summers
 */
public class QueryMap extends HashMap<String, String> {

    public QueryMap(Class parent) {
        this(parent, parent.getSimpleName() + ".sql");
    }

    public QueryMap(Class parent, String name) {
        try {
            InputStream stream = parent.getResourceAsStream(parent.getSimpleName() + ".sql");
            BufferedReader reader = Streams.newBufferedReader(stream);
            StringBuilder builder = null;
            String key = null;
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                } else if (line.trim().length() == 0) {
                    if (key != null) {
                        put(key, builder.toString().trim());
                        key = null;
                    }
                } else if (line.startsWith("--")) {
                    if (line.length() > 3) {
                        key = line.substring(2).trim();
                        builder = new StringBuilder();
                    }
                } else if (key != null) {
                    builder.append(line);
                    builder.append("\n");
                }
            }
            if (key != null) {
                put(key, builder.toString());
            }
        } catch (Exception e) {
            throw Exceptions.newRuntimeException(e);
        }
    }
}
