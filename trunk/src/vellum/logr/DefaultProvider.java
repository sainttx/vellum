/*
 * Apache Software License 2.0
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 */

package vellum.logr;

import vellum.logr.*;

/**
 *
 * @author evan.summers
 */
public class DefaultProvider implements LogrProvider {

    public DefaultProvider() {
    }
    
    @Override
    public Logr getLogger(LogrContext context) {
        return new LogrAdapter(context, new DefaultHandler());
    }

}
