
package saltserver.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpsExchange;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import vellum.storage.ConnectionPool;
import java.io.PrintStream;
import java.sql.*;
import saltserver.app.SecretApp;
import saltserver.app.SecretPageHandler;
import sun.security.x509.X500Name;
import vellum.httpserver.HttpExchangeInfo;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.storage.ConnectionEntry;

/**
 *
 * 
 */
public class SecretAdminHandler implements HttpHandler {
    Logr logger = LogrFactory.getLogger(getClass());
    HttpExchange httpExchange;
    HttpExchangeInfo httpExchangeInfo;
    SecretPageHandler handler;
    PrintStream out;
    ConnectionPool connectionPool;
    ConnectionEntry connectionEntry;
    Connection connection;
    DatabaseMetaData databaseMetaData;
    int revisionNumber;
    
    public SecretAdminHandler(SecretApp app) {
        super();
        this.connectionPool = app.getStorage().getConnectionPool();
    }
    
    @Override
    public void handle(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        out = httpExchangeInfo.getPrintStream();
        handler = new SecretPageHandler(httpExchange);
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
        out.printf("<h3>%s</h3>\n", new X500Name(httpsExchange.getSSLSession().getPeerPrincipal().getName()).getCommonName());
        out.printf("<form action='/admin' method='post'>\n");
        out.printf("<input type='password' name='password' width='40' placeholder='Cipher passphrase'>\n");
        out.printf("<input type='submit' value='Enable server'>\n");
        out.printf("<br><input type='submit' value='Disable server'>\n");
        out.printf("</form>\n");
    }
    
}
