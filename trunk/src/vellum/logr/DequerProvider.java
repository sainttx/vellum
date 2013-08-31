/*
 * Apache Software License 2.0
 * Licensed to the Apache Software Foundation by Evan Summers, for ASL 2.0.
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
