/*
 * Apache Software License 2.0
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 */

package vellum.logr;

/**
 *
 * @author evan.summers
 */
public interface LogrProvider {

    public Logr getLogger(LogrContext context);
}
