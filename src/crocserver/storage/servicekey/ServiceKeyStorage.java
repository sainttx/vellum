/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.servicekey;

import crocserver.storage.CrocStorage;
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
public class ServiceKeyStorage {

    static QueryMap sqlMap = new QueryMap(ServiceKeyStorage.class);
    Logr logger = LogrFactory.getLogger(ServiceKeyStorage.class);
    CrocStorage storage;

    public ServiceKeyStorage(CrocStorage storage) {
        this.storage = storage;
    }

    private ServiceKey build(ResultSet resultSet) throws SQLException {
        ServiceKey seviceKey = new ServiceKey();
        seviceKey.setId(resultSet.getLong(ServiceKeyDatum.service_key_id.name()));
        seviceKey.setAdminUserName(resultSet.getString(ServiceKeyDatum.username.name()));
        seviceKey.setHostName(resultSet.getString(ServiceKeyDatum.host_.name()));
        seviceKey.setServiceName(resultSet.getString(ServiceKeyDatum.service.name()));
        seviceKey.setPublicKey(resultSet.getString(ServiceKeyDatum.public_key.name()));
        return seviceKey;
    }
    
    public ServiceKey find(long id) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(ServiceKeyQuery.find_id.name()));
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
    
    public ServiceKey find(String userName, String hostName, String serviceName) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(ServiceKeyQuery.find_username_host_service.name()));
            statement.setString(1, userName);
            statement.setString(2, hostName);
            statement.setString(3, serviceName);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            return build(resultSet);
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }
    
    public void insert(ServiceKey serviceRecord) throws StorageException, SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(ServiceKeyQuery.insert.name()));
            statement.setString(1, serviceRecord.getAdminUserName());
            statement.setString(2, serviceRecord.getHostName());
            statement.setString(3, serviceRecord.getServiceName());
            statement.setString(4, serviceRecord.getPublicKey());
            int insertCount = statement.executeUpdate();
            if (insertCount != 1) {
                throw new StorageException(StorageExceptionType.NOT_INSERTED);
            }
            ok = true;
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    public List<ServiceKey> getList(String userName) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            List<ServiceKey> list = new ArrayList();
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(ServiceKeyQuery.list_by_username.name()));
            statement.setString(1, userName);
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
