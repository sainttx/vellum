/*
 * Apache Software License 2.0, (c) Copyright 2012, Evan Summers
 * 
 */
package crocserver.storage.servicekey;

import crocserver.storage.CrocStorage;
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
public class ServiceCertStorage {

    static QueryMap sqlMap = new QueryMap(ServiceCertStorage.class);
    Logr logger = LogrFactory.getLogger(ServiceCertStorage.class);
    CrocStorage storage;

    public ServiceCertStorage(CrocStorage storage) {
        this.storage = storage;
    }

    private ServiceCert build(ResultSet resultSet) throws SQLException {
        ServiceCert serviceCert = new ServiceCert();
        serviceCert.setId(resultSet.getLong(ServiceCertMeta.service_cert_id.name()));
        serviceCert.setOrgId(resultSet.getLong(ServiceCertMeta.org_id.name()));
        serviceCert.setHostName(resultSet.getString(ServiceCertMeta.host_name.name()));
        serviceCert.setServiceName(resultSet.getString(ServiceCertMeta.service_name.name()));
        serviceCert.setDname(resultSet.getString(ServiceCertMeta.dname.name()));
        serviceCert.setCert(resultSet.getString(ServiceCertMeta.cert.name()));
        serviceCert.setUpdated(resultSet.getTimestamp(ServiceCertMeta.updated.name()));
        serviceCert.setUpdatedBy(resultSet.getString(ServiceCertMeta.updated_by.name()));
        return serviceCert;
    }
    
    public void insert(String updatedBy, Org org, ServiceCert serviceCert) throws StorageException, SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(ServiceCertQuery.insert.name()));
            int index = 0;
            statement.setLong(++index, org.getId());
            statement.setString(++index, serviceCert.getHostName());
            statement.setString(++index, serviceCert.getServiceName());
            statement.setString(++index, serviceCert.getDname());
            statement.setString(++index, serviceCert.getCert());
            statement.setString(++index, updatedBy);
            int insertCount = statement.executeUpdate();
            if (insertCount != 1) {
                throw new StorageException(StorageExceptionType.NOT_INSERTED);
            }
            ok = true;
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }
    }

    public void updateCert(String userName, ServiceCert serviceCert) throws SQLException {
        logger.verbose("updateCert", userName, serviceCert.getId());
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(ServiceCertQuery.update_cert.name()));
            int index = 0;
            statement.setString(++index, serviceCert.getDname());
            statement.setString(++index, serviceCert.getCert());
            statement.setString(++index, userName);
            statement.setLong(++index, serviceCert.getId());
            int updateCount = statement.executeUpdate();
            if (updateCount != 1) {
                throw new StorageException(StorageExceptionType.UPDATE_COUNT);
            }
        } finally {
            storage.getConnectionPool().releaseConnection(connection, ok);
        }  
    }

    public ServiceCert findDname(String dname) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(ServiceCertQuery.find_id.name()));
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
        
    public ServiceCert find(long id) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(sqlMap.get(ServiceCertQuery.find_id.name()));
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
    
    public ServiceCert find(long orgId, String hostName, String serviceName) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(ServiceCertQuery.find_org_host_service.name()));
            statement.setLong(1, orgId);
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
    
    public List<ServiceCert> getList() throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            List<ServiceCert> list = new ArrayList();
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(ServiceCertQuery.list.name()));
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

    public List<ServiceCert> getList(long orgId) throws SQLException {
        Connection connection = storage.getConnectionPool().getConnection();
        boolean ok = false;
        try {
            List<ServiceCert> list = new ArrayList();
            PreparedStatement statement = connection.prepareStatement(
                    sqlMap.get(ServiceCertQuery.list_org.name()));
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
