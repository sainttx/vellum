
package saltserver.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import vellum.storage.ConnectionPool;
import java.io.PrintStream;
import java.sql.*;
import saltserver.app.SecretApp;
import saltserver.app.SecretPageHandler;
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
        handler = new SecretPageHandler(httpExchange);
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        out = httpExchangeInfo.getPrintStream();
        logger.info("handle", getClass().getSimpleName(), httpExchangeInfo.getPath());
        try {
            httpExchangeInfo.sendResponse("text/html", true);
            handle();
        } catch (Exception e) {
            handler.handleException(e);
        }
        httpExchange.close();
    }
    
    private void handle() throws IOException {
        httpExchangeInfo.sendResponse("text/html", true);
        handler.printPageHeader("Admin");
        
    }
    
}
