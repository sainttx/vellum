
package mantra.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.PrintStream;
import mantra.app.MantraApp;
import mantra.app.MantraPageHandler;
import vellum.httpserver.HttpExchangeInfo;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.util.Strings;

/**
 *
 * 
 */
public class AdminHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    MantraPageHandler handler;
    MantraApp app;
    PrintStream out;
    
    public AdminHandler(MantraApp app) {
        super();
        this.app = app;
    }
    
    @Override
    public void handle(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        out = httpExchangeInfo.getPrintStream();
        handler = new MantraPageHandler(httpExchange);
        logger.info("handle", getClass().getSimpleName(), httpExchangeInfo.getPath());
        try {
            handle();
        } catch (Exception e) {
            handler.handleException(e);
        }
        httpExchange.close();
    }
    
    private void handle() throws Exception {
        handler.printPageHeader("Admin");
        String username = httpExchangeInfo.getParameterMap().get("username");
        String password = httpExchangeInfo.getParameterMap().get("password");
        String keyStorePassword = httpExchangeInfo.getParameterMap().get("keyStorePassword");
        logger.info("handle", app.getKeyStoreManager().getKeyStorePath());
        if (!Strings.isEmpty(keyStorePassword)) {
            app.getKeyStoreManager().loadKeyStore(keyStorePassword.toCharArray());
        }
        logger.info("username", username);
        if (username != null && password != null) {
            app.getPasswordManager().put(username, password.toCharArray());
        }
        out.printf("<h3>%s %s</h3>\n", getClass().getSimpleName(),
                app.getKeyStoreManager().getKeyStorePath());
        out.printf("<form action='/admin' method='POST'>\n");
        out.printf("<input type='text' name='username' width='40' placeholder='Username'>\n");
        out.printf("<input type='keyStorePassword' name='keyStorePassword' width='40' placeholder='KeyStore Password'>\n");
        out.printf("<input type='password' name='password' width='40' placeholder='Passphrase'>\n");
        out.printf("<input type='submit' value='Send password'>\n");
        out.printf("</form>\n");
        out.printf("<h3>Passwords on hand</h3>\n");
        if (app.getPasswordManager().getPasswordMap().isEmpty()) {
                out.printf("None<br>\n");
        } else {
            for (String key : app.getPasswordManager().getPasswordMap().keySet()) {
                out.printf("<span>%s</span><br>\n", key);
            }
        }
    }    
}
