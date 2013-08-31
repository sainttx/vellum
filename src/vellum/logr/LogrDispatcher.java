/*
 * Apache Software License 2.0
 *    https://code.google.com/p/vellum - Contributed by Evan Summers
 */
package vellum.logr;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author evan.summers
 */
public class LogrDispatcher implements LogrHandler {
    List<LogrHandler> handlerList = new CopyOnWriteArrayList();
    
    @Override
    public void handle(LogrContext context, LogrRecord message) {
        for (LogrHandler handler : handlerList) {
            handler.handle(context, message);
        }
    }

    public List<LogrHandler> getHandlerList() {
        return handlerList;
    }
 
}
