
package saltserver.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;
import java.io.IOException;
import java.io.PrintStream;
import saltserver.app.VaultApp;
import saltserver.app.VaultPageHandler;
import sun.security.x509.X500Name;
import vellum.httpserver.HttpExchangeInfo;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * 
 */
public class AdminHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    VaultPageHandler handler;
    VaultApp app;
    PrintStream out;
    
    public AdminHandler(VaultApp app) {
        super();
        this.app = app;
    }
    
    @Override
    public void handle(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        out = httpExchangeInfo.getPrintStream();
        handler = new VaultPageHandler(httpExchange);
        logger.info("handle", getClass().getSimpleName(), httpExchangeInfo.getPath());
        try {
            handle();
        } catch (Exception e) {
            handler.handleException(e);
        }
        httpExchange.close();
    }
    
    private void handle() throws IOException {
        handler.printPageHeader("Admin");
        HttpsExchange httpsExchange = (HttpsExchange) httpExchange;
        String principalName = new X500Name(httpsExchange.getSSLSession().getPeerPrincipal().getName()).getCommonName();
        String password = httpExchangeInfo.getParameterMap().get("password");
        if (password != null) {
            app.getPasswordManager().getPasswordMap().put(principalName, password.toCharArray());
        }
        out.printf("<h3>%s</h3>\n", principalName);
        out.printf("<form action='/admin' method='post'>\n");
        out.printf("<input type='password' name='password' width='40' placeholder='Cipher passphrase'>\n");
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
