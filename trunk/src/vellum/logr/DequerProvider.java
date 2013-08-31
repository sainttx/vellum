/*
 * Apache Software License 2.0
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
 */

package vellum.logr;

import vellum.logr.*;

/**
 *
 * @author evan.summers
 */
public class DequerProvider implements LogrProvider {
    LogrDispatcher dispatcher = new LogrDispatcher();
    DequerHandler dequerHandler = new DequerHandler();
    
    public DequerProvider() {
        dispatcher.getHandlerList().add(new DefaultHandler());
        dispatcher.getHandlerList().add(dequerHandler);
    }
    
    @Override
    public Logr getLogger(LogrContext context) {
        return new LogrAdapter(context, dispatcher);
    }

    public DequerHandler getDequerHandler() {
        return dequerHandler;
    }    
}
