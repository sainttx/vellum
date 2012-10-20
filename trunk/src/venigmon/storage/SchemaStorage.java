/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package venigmon.storage;

import vellum.query.RowSets;
import java.io.InputStream;
import java.sql.*;
import javax.sql.RowSet;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;

/**
 *
 * @author evan
 */
public class SchemaStorage {

    static final int MIN_VERSION_NUMBER = 0;
    static final int CURRENT_VERSION_NUMBER = 2;
    Logr logger = LogrFactory.getLogger(SchemaStorage.class);
    VenigmonStorage storage;
    DatabaseMetaData databaseMetaData;

    public SchemaStorage(VenigmonStorage storage) {
        this.storage = storage;
    }

    public void verifySchema() throws Exception {
        if (MIN_VERSION_NUMBER == 0) {
            createSchema();
        } else if (verifySchemaVersion()) {
        } else {
            createSchema();
        }
    }

    private boolean verifySchemaVersion() throws Exception {
        Connection connection = storage.getConnection();
        try {
            databaseMetaData = connection.getMetaData();
            logger.info("databaseProductName " + databaseMetaData.getDatabaseProductName());
            logger.info("databaseProductVersion " + databaseMetaData.getDatabaseProductVersion());
            logger.info("url " + databaseMetaData.getURL());
            logger.info("userName " + databaseMetaData.getUserName());
            RowSet rowSet = RowSets.getRowSet(connection, "select * from schema_revision order by update_time desc");
            rowSet.first();
            int versionNumber = rowSet.getInt(1);
            ResultSet resultSet = databaseMetaData.getCatalogs();
            String catalog = null;
            while (resultSet.next()) {
                catalog = resultSet.getString(1);
                logger.info(catalog);
            }
            return versionNumber >= MIN_VERSION_NUMBER;
        } finally {
            storage.releaseConnection(connection);
        }
    }

    private void createSchema() throws Exception {
        Connection connection = storage.getConnection();
        try {
            String sqlScriptName = "create.sql";
            InputStream stream = getClass().getResourceAsStream(sqlScriptName);
            logger.verbose(sqlScriptName);
            byte[] bytes = new byte[stream.available()];
            stream.read(bytes);
            String sql = new String(bytes);
            String[] sqlStatements = sql.split(";");
            for (String sqlStatement : sqlStatements) {
                sqlStatement = sqlStatement.trim();
                if (!sqlStatement.isEmpty()) {
                    logger.verbose(sqlStatement);
                    try {
                        connection.createStatement().execute(sqlStatement);
                    } catch (SQLException e) {
                        logger.warn(e.getMessage());
                    }
                }
            }
            String insertSchemaVersion = "insert into schema_revision (revision_number) values (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSchemaVersion);
            preparedStatement.setInt(1, CURRENT_VERSION_NUMBER);
            preparedStatement.execute();
        } finally {
            storage.releaseConnection(connection);
        }
    }
}
