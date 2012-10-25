
package venigmon.httpserver;

import vellum.util.Types;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Date;
import javax.sql.RowSet;
import vellum.html.TablePrinter;
import vellum.storage.ConnectionPool;
import vellum.query.RowSets;
import vellum.storage.StorageException;
import vellum.storage.StorageExceptionType;
import venigmon.storage.CrocStorage;

/**
 *
 * 
 */
public class StoragePageHandler extends AbstractPageHandler {

    ConnectionPool connectionPool;
    Connection connection;
    DatabaseMetaData databaseMetaData;
    int revisionNumber;
    
    public StoragePageHandler(CrocStorage storage) {
        super();
        this.connectionPool = storage.getConnectionPool();
    }
    
    @Override
    protected void handle() throws Exception {
        connection = connectionPool.getConnection();
        boolean ok = false;
        try {
            connection = connectionPool.getConnection();
            queryDatabaseTime();
            print(RowSets.getRowSet(connection, "select * from service_record"));
            printSchema();
            ok = true;
        } finally {
            connectionPool.releaseConnection(connection, ok);
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
        RowSet rowSet = RowSets.getRowSet(connection, "select * from schema_revision order by update_time desc");
        if (rowSet.next()) {
            return rowSet.getInt(1);        
        }
        throw new StorageException(StorageExceptionType.NOT_FOUND);
    }

    private void printSchema() throws Exception {
        databaseMetaData = connection.getMetaData();
        RowSet rowSet = RowSets.getRowSet(connection, "select * from schema_revision order by update_time desc");
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
        TablePrinter printer = new TablePrinter(out);
        printer.table();
        printer.thead();
        for (int i = 0; i < columnNames.length; i++) {
            printer.th(columnNames[i]);
        }
        printer.theadClose();
        printer.tbody();
        while (resultSet.next()) {      
            printer.tr();
            for (int i = 0; i < columnNames.length; i++) {
                Object value = resultSet.getObject(columnNames[i]);
                printer.td(Types.getStyleClass(value.getClass()), value);
            }
            printer.trClose();
        }
        printer.tbodyClose();
        printer.tableClose();        
    }
    
    private void print(ResultSet resultSet) throws Exception {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        TablePrinter tablePrinter = new TablePrinter(out);
        tablePrinter.tableDiv("resultSet");
        tablePrinter.thead();
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
            tablePrinter.th(resultSetMetaData.getColumnName(i));
        }
        tablePrinter.theadClose();
        tablePrinter.tbody();
        while (resultSet.next()) {      
            tablePrinter.tr();
            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                tablePrinter.td(resultSetMetaData.getColumnClassName(i), resultSet.getObject(i));
            }
            tablePrinter.trClose();
        }
        tablePrinter.tbodyClose();
        tablePrinter.tableDivClose();
    }
    
}
