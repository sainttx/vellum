
package saltserver.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import crocserver.httphandler.common.AbstractPageHandler;
import java.io.IOException;
import vellum.util.Types;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Date;
import javax.sql.RowSet;
import vellum.html.HtmlPrinter;
import vellum.storage.ConnectionPool;
import vellum.query.RowSets;
import vellum.storage.StorageException;
import vellum.storage.StorageExceptionType;
import java.io.PrintStream;
import java.sql.*;
import saltserver.app.SaltApp;
import vellum.httpserver.HttpExchangeInfo;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.storage.ConnectionEntry;
import vellum.util.Streams;

/**
 *
 * 
 */
public class SaltManagerHandler implements HttpHandler {
    public static int COLUMN_LIMIT = 8;
    Logr logger = LogrFactory.getLogger(getClass());
    HttpExchangeInfo httpExchangeInfo;
    PrintStream out;
    ConnectionPool connectionPool;
    ConnectionEntry connectionEntry;
    Connection connection;
    DatabaseMetaData databaseMetaData;
    int revisionNumber;
    
    public SaltManagerHandler(SaltApp app) {
        super();
        this.connectionPool = app.getStorage().getConnectionPool();
    }
    
    @Override
    public void handle(HttpExchange httpExchange) {
        httpExchangeInfo = new HttpExchangeInfo(httpExchange);
        out = httpExchangeInfo.getPrintStream();
        logger.info("handle", getClass().getSimpleName(), httpExchangeInfo.getPath());
        try {
            httpExchangeInfo.sendResponse("text/html", true);
            printPageHeader();
            handle();
        } catch (Exception e) {
            httpExchangeInfo.handleException(e);
        }
        httpExchange.close();
    }

    protected void printPageHeader() throws IOException {
        out.println("<html>");
        out.println("<head>");
        out.printf("<title>%s</title>", getClass().getSimpleName());
        out.printf("<style>\n%s\n</style>\n", Streams.readString(getClass(), "style.css"));
        out.println("</head>");
        out.println("<body>");
    }
    
    private void handle() throws Exception {
        connectionEntry = connectionPool.takeEntry();
        connection = connectionEntry.getConnection();
        try {
            queryDatabaseTime();
            print(RowSets.getRowSet(connection, "select * from schema_revision"));
            print(RowSets.getRowSet(connection, "select * from secret"));
            printSchema();
            connectionEntry.setOk(true);
        } finally {
            connectionPool.releaseConnection(connectionEntry);
        }
    }

    private Date queryDatabaseTime() throws Exception {
        Statement stmt = connection.createStatement();
        ResultSet rst = stmt.executeQuery("select now()");
        while (rst.next()) {
            return new Date(rst.getTimestamp(1).getTime());
        }
        throw new StorageException(StorageExceptionType.NOT_FOUND);
    }
    
    private int querySchemaRevisionNumber() throws Exception {
        RowSet rowSet = RowSets.getRowSet(connection, "select * from schema_revision order by updated desc");
        if (rowSet.next()) {
            return rowSet.getInt(1);
        }
        throw new StorageException(StorageExceptionType.NOT_FOUND);
    }

    private void printSchema() throws Exception {
        databaseMetaData = connection.getMetaData();
        RowSet rowSet = RowSets.getRowSet(connection, "select * from schema_revision order by updated desc");
        print(rowSet);
        rowSet.first();
        revisionNumber = rowSet.getInt(1);
        out.println("<pre>");
        out.println("databaseProductName " + databaseMetaData.getDatabaseProductName());
        out.println("databaseProductVersion " + databaseMetaData.getDatabaseProductVersion());
        out.println("url " + databaseMetaData.getURL());
        out.println("userName " + databaseMetaData.getUserName());
        out.println("revisionNumber " + querySchemaRevisionNumber());
        out.println("time " + queryDatabaseTime());
        out.println("</pre>");
        ResultSet resultSet = databaseMetaData.getCatalogs();
        while (resultSet.next()) {            
            String catalog = resultSet.getString(1);
            out.printf("<h2>%s</h2>\n", catalog);
            printColumns(catalog);
        }
    }
    
    private void printTables(String catalog) throws Exception {
        ResultSet tablesResultSet = databaseMetaData.getTables(catalog, null, null, null);
        print(tablesResultSet, new String[] {"TABLE_NAME"});
    }
    
    private void printColumns(String catalog) throws Exception {
        ResultSet resultSet = databaseMetaData.getColumns(catalog, "PUBLIC", "%", "%");
        print(resultSet, new String[] {"TABLE_NAME", "COLUMN_NAME", "TYPE_NAME", "COLUMN_SIZE"});
    }

    private void print(ResultSet resultSet, String[] columnNames) throws Exception {
        HtmlPrinter h = new HtmlPrinter(out);
        h.table("resultSet");
        h.thead();
        for (int i = 0; i < columnNames.length && i < COLUMN_LIMIT; i++) {
            h.th(columnNames[i]);
        }
        h._thead();
        h.tbody();
        while (resultSet.next()) {      
            h.tr();
            for (int i = 0; i < columnNames.length && i < COLUMN_LIMIT; i++) {
                Object value = resultSet.getObject(columnNames[i]);
                h.td(Types.getStyleClass(value.getClass()), value);
            }
            h._tr();
        }
        h._tbody();
        h._table();
    }

    private Object trim(Object object) {
        if (object instanceof String) {
            String string = (String) object;
            if (string.length() > 32) {
                return string.substring(0, 32);
            }
        }
        return object;
    }
        
    private void print(ResultSet resultSet) throws Exception {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        HtmlPrinter tablePrinter = new HtmlPrinter(out);
        tablePrinter.tableDiv("resultSet");
        tablePrinter.thead();
        for (int i = 1; i <= resultSetMetaData.getColumnCount() && i < COLUMN_LIMIT; i++) {
            tablePrinter.th(resultSetMetaData.getColumnName(i));
        }
        tablePrinter._thead();
        tablePrinter.tbody();
        while (resultSet.next()) {      
            tablePrinter.tr();
            for (int i = 1; i <= resultSetMetaData.getColumnCount() && i < COLUMN_LIMIT; i++) {
                tablePrinter.td(resultSetMetaData.getColumnClassName(i), trim(resultSet.getObject(i)));
            }
            tablePrinter._tr();
        }
        tablePrinter._tbody();
        tablePrinter._tableDiv();
    }
}
