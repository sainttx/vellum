/*
 * Apache Software License 2.0
 * Vellum by Evan Summers under Apache Software License 2.0 from ASF.
 */

package vellum.logr;

import java.io.PrintStream;

/**
 *
 * @author evan.summers
 */
public class DefaultHandler implements LogrHandler {
    PrintStream out = System.out;
    DefaultFormatter formatter = new DefaultFormatter();
    
    public DefaultHandler() {
    }

    @Override
    public void handle(LogrContext context, LogrRecord record) {
        if (record.getLevel().ordinal() >= context.getLevel().ordinal()) {
            out.println(formatter.format(context, record));
            if (record.getThrowable() != null) {
                record.getThrowable().printStackTrace(out);
            }
        }
    }
    
    Throwable getThrowable(Object[] args) {
        if (args.length > 0 && args[0] instanceof Throwable) {
            return (Throwable) args[0];
        }
        return null;
    }    
}
