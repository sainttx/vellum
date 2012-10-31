/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.servicecert;

import crocserver.storage.common.CrocStorage;
import crocserver.storage.org.Org;
import java.security.Principal;
import java.sql.*;
import vellum.storage.StorageExceptionType;
import java.util.ArrayList;
import java.util.List;
import vellum.query.QueryMap;
import vellum.logr.Logr;
import vellum.logr.LogrFactory;
import vellum.storage.StorageException;

/**
 *
 * @author evan
 */
public class ClientCertStorage {

    static QueryMap sqlMap = new QueryMap(ClientCertStorage.class);
    Logr logger = LogrFactory.getLogger(ClientCertStorage.class);
    CrocStorage storage;

    public ClientCertStorage(CrocStorage storage) {
        this.storage = storage;
    }

    private ClientCert build(ResultSet resultSet) throws SQLException {
        ClientCert cert = new ClientCert();
        cert.setId(resultSet.getLong(ClientCertMeta.client_cert_id.name()));
        cert.setOrgId(resultSet.getLong(ClientCertMeta.org_id.name()));
        cert.setHostName(resultSet.getString(ClientCertMeta.host_name.name()));
        cert.setClientName(resultSet.getString(ClientCertMeta.client_name.name()));
        cert.setDname(resultSet.getString(ClientCertMeta.dname.name()));
        cert.setCert(resultSet.getString(ClientCertMeta.cert.name()));
        cert.setUpdated(resultSet.getTimestamp(ClientCertMeta.updated.name()));
        cert.setUpdatedBy(resultSet.getString(ClientCertMeta.updated_by.name()));
        cert.setStored(true);
        return cert;
    }

    public void insert(String updatedBy, Org org, ClientCert cert) throws StorageException, SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(ClientCertQuery.insert.name()));
            int index = 0;
            statement.setLong(++index, org.getId());
            statement.setString(++index, cert.getHostName());
            statement.setString(++index, cert.getClientName());
            statement.setString(++index, cert.getDname());
            statement.setString(++index, cert.getCert());
            statement.setString(++index, updatedBy);
            int insertCount = statement.executeUpdate();
            if (insertCount != 1) {
                throw new StorageException(StorageExceptionType.NOT_INSERTED);
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            cert.setId(generatedKeys.getLong((1)));
            cert.setStored(true);
            ok = true;
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    public void updateCert(String userName, ClientCert cert) throws SQLException {
        logger.verbose("updateCert", userName, cert.getId());
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(ClientCertQuery.update_cert.name()));
            int index = 0;
            statement.setString(++index, cert.getDname());
            statement.setString(++index, cert.getCert());
            statement.setString(++index, userName);
            statement.setLong(++index, cert.getId());
            int updateCount = statement.executeUpdate();
            if (updateCount != 1) {
                throw new StorageException(StorageExceptionType.UPDATE_COUNT);
            }
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    public ClientCert findDname(String dname) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(ClientCertQuery.find_dname.name()));
            statement.setString(1, dname);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return build(resultSet);
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    public ClientCert find(long id) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(ClientCertQuery.find_id.name()));
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return build(resultSet);
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    public ClientCert find(long orgId, String hostName, String clientName) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(ClientCertQuery.find_org_host_client.name()));
            statement.setLong(1, orgId);
            statement.setString(2, hostName);
            statement.setString(3, clientName);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return build(resultSet);
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    public List<ClientCert> getList() throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            List<ClientCert> list = new ArrayList();
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(ClientCertQuery.list.name()));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                list.add(build(resultSet));
            }
            ok = true;
            return list;
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    public List<ClientCert> getList(long orgId) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            List<ClientCert> list = new ArrayList();
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(ClientCertQuery.list_org.name()));
            statement.setLong(1, orgId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                list.add(build(resultSet));
            }
            ok = true;
            return list;
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }
}
