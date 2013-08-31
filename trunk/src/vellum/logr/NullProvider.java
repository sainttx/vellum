/*
 * Apache Software License 2.0
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 */
package vellum.logr;

/**
 *
 * @author evan.summers
 */
public class NullProvider implements LogrProvider {

    @Override
    public Logr getLogger(LogrContext context) {
        return new LogrAdapter(context, new NullLogrHandler());
    }

}
