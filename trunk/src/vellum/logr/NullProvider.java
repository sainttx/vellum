/*
 * Apache Software License 2.0
 * https://code.google.com/p/vellum - Contributed (2013) by Evan Summers to ASF
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
