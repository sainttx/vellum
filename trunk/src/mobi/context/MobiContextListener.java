
/*
 * Contributed (2013) by Evan Summers via https://code.google.com/p/vellum
 * 
 */
package mobi.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import mobi.session.Servlets;


/**
 *
 * @author evan.summers
 */
public class MobiContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Servlets.contextInitialized(sce);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    
}
