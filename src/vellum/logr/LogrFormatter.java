/*
 */
package vellum.logr;

/**
 *
 * @author evan.summers
 */
public interface LogrFormatter {
    public String format(LogrContext context, LogrRecord message);
}
